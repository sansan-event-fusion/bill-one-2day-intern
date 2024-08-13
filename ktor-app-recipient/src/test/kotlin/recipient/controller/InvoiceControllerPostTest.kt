package recipient.controller

import com.google.cloud.storage.BlobId
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.testing.*
import io.ktor.utils.io.streams.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import recipient.domain.tenant.TenantNameId
import recipient.fixture.RecipientTableFixture
import recipient.fixture.TenantTableFixture
import recipient.fixture.insertRecipient
import recipient.fixture.insertTenant
import recipient.module
import recipient.testing.Database
import recipient.testing.testSettings
import recipient.util.getStorageObject
import recipient.util.withHandle

class InvoiceControllerPostTest {
    companion object {
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        private lateinit var tenant: TenantTableFixture
        private lateinit var recipient: RecipientTableFixture

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            database.tearDown()
        }
    }

    @Before
    fun before() {
        database.setUpWithDsl {
            tenant = insertTenant("yonyon") associates {
                recipient = insertRecipient()

            }
        }
    }

    @Test
    fun `can upload invoice`(): Unit = withTestApplication({ module(true, settings) }) {
        with(
            handleRequest(HttpMethod.Post, "/api/recipient/invoice/upload") {
                val boundary = "***bbb***"

                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                addHeader("X-App-Tenant-Name-Id", tenant.tenantNameId)
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
                addHeader("X-Cloud-Trace-Context", "430963703d1a5d41fa9f63cda15a214d/0;o=1")
                setBody(
                    boundary,
                    listOf(
                        PartData.FileItem(
                            {
                                javaClass.classLoader.getResource("pdf/upload.pdf")!!.readBytes().inputStream()
                                    .asInput()
                            },
                            {},
                            headersOf(
                                HttpHeaders.ContentDisposition,
                                ContentDisposition.File
                                    .withParameter(ContentDisposition.Parameters.Name, "invoice")
                                    .withParameter(ContentDisposition.Parameters.FileName, "upload.pdf")
                                    .toString()
                            )
                        ),
                        PartData.FormItem(
                            tenant.tenantUuid.toString(),
                            {},
                            headersOf(
                                HttpHeaders.ContentDisposition,
                                ContentDisposition.Inline
                                    .withParameter(ContentDisposition.Parameters.Name, "supplierUUID").toString()
                            )
                        ),
                        PartData.FormItem(
                            tenant.tenantNameId,
                            {},
                            headersOf(
                                HttpHeaders.ContentDisposition,
                                ContentDisposition.Inline
                                    .withParameter(ContentDisposition.Parameters.Name, "supplierName").toString()
                            )
                        ),
                        PartData.FormItem(
                            "1000",
                            {},
                            headersOf(
                                HttpHeaders.ContentDisposition,
                                ContentDisposition.Inline
                                    .withParameter(ContentDisposition.Parameters.Name, "invoiceAmount").toString()
                            )
                        )
                    )
                )
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.Created)
            withHandle(tenantNameId = TenantNameId("yonyon")) { handle ->
                val invoice = handle.createQuery("SELECT * FROM invoice WHERE recipient_uuid = :recipientUUID")
                    .bind("recipientUUID", recipient.recipientUuid)
                    .mapToMap()
                    .singleOrNull()

                assertThat(invoice).isNotNull()
                assertThat(invoice!!["recipient_uuid"]).isEqualTo(recipient.recipientUuid)
                assertThat(invoice!!["tenant_name_id"]).isEqualTo("yonyon")
                val storageObjectSize = getStorageObject(
                    BlobId.of(
                        "recipient-test-bucket",
                        "recipient/${recipient.recipientUuid}/recipient-invoices/${invoice["invoice_uuid"]}.pdf"
                    )
                ).size
                assertThat(storageObjectSize).isEqualTo(63341)
            }
        }
    }

    @Test
    fun `return BadRequest without invoice`(): Unit = withTestApplication({ module(true, settings) }) {
        with(
            handleRequest(HttpMethod.Post, "/api/recipient/invoice/upload") {
                val boundary = "***bbb***"

                addHeader(
                    HttpHeaders.ContentType,
                    ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
                )
                addHeader("X-App-Tenant-Name-Id", tenant.tenantNameId)
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
                addHeader("X-Cloud-Trace-Context", "430963703d1a5d41fa9f63cda15a214d/0;o=1")
                setBody(
                    """
                    {
                        "recipientUUID": "recipient-uuid",
                        "invoiceUUID": "invoice-uuid",
                        "registeredAt": "2019-01-01 09:00:00+09"
                    }
                    """.trimIndent()
                )
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
        }

    }

}
