package issuing.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.tenant.TenantNameId
import issuing.fixture.*
import issuing.infrastructure.IssuerInvoiceRow
import issuing.module
import issuing.testing.Database
import issuing.testing.testSettings
import issuing.util.withHandle
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.core.kotlin.mapTo
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.AfterAll
import java.time.OffsetDateTime
import java.util.*

class IssuerInvoiceReflectPostTest {
  private val issuerInvoiceUUID = IssuerInvoiceUUID(UUID.randomUUID())
  private val callUUID = UUID.randomUUID()

  companion object {
    private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
    private val settings = testSettings(schemaName)
    private val database = Database(settings, schemaName)

    private lateinit var issuerTenant: TenantTableFixture
    private lateinit var issuer: IssuerTableFixture
    private lateinit var issuerInvoice: IssuerInvoiceTableFixture

    private lateinit var recipientTenant: TenantTableFixture
    private lateinit var recipient: RecipientTableFixture

    @JvmStatic
    @AfterAll
    fun afterAll() {
      database.tearDown()
    }
  }

  @Before
  fun before() {
    database.setUpWithDsl {
      recipientTenant = insertTenant("gogo") associates {
        recipient = insertRecipient("受領者1", "recipient@email.com")
      }
      issuerTenant = insertTenant("yonyon") associates {
        issuer = insertIssuer("発行太郎", "issuer@email.com") associates {
          issuerInvoice =
            insertIssuerInvoice(
              recipientTenantNameId = "gogo",
              issuerInvoiceUuid = issuerInvoiceUUID.value,
              invoiceAmount = 1000,
              paymentDeadline = OffsetDateTime.now(),
              uploadedFileStoragePath = null,
            )
        }
      }
    }
  }

  @Test
  fun `should return 200 when success to reflect issued result`(): Unit =
    withTestApplication({ module(true, settings) }) {
      val fileUploadPath =
        "http://localhost:4443/issuing-dev-bucket/issuing/${issuer.issuerUuid}/issuing-invoice/${issuerInvoiceUUID.value}.pdf"
      val bodyEncoded =
        Base64.getEncoder().encodeToString(
          """
                    {
                        "generatedInvoiceUUID": "${issuerInvoiceUUID.value}",
                        "generatedResult": "SUCCEEDED",
                        "uploadedFilePathIssuing": "$fileUploadPath"
                    }
                    """.trimIndent().toByteArray(),
        )

      val testRequest =
        """
                {
                    "task": {
                        "httpRequest": {
                            "httpMethod": 1,
                            "url": "/event-handler/issuing/invoices/reflect-issued-result",
                            "headers": {
                                "X-Tenant-Name-Id": "gogo",
                                "X-Cloud-Trace-Context": "$callUUID/0;o=1"
                            },
                            "body": "$bodyEncoded"
                        }
                    }
                }
                """.trimIndent()

      val call =
        handleRequest(HttpMethod.Post, "/event-handler/issuing/invoices/reflect-issued-result") {
          addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
          setBody(testRequest)
        }

      assertEquals(HttpStatusCode.OK, call.response.status())
      withHandle(tenantNameId = TenantNameId("yonyon")) { handle ->
        val issuerInvoiceRow =
          handle.createQuery("SELECT * FROM issuer_invoice")
            .mapTo<IssuerInvoiceRow>().one()

        assertThat(issuerInvoiceRow.uploadedFileStoragePath).isEqualTo(fileUploadPath)
      }
    }

  @Test
  fun `should return 400 when failed to reflect issued result`(): Unit =
    withTestApplication({ module(true, settings) }) {
      val bodyEncoded =
        Base64.getEncoder().encodeToString(
          """
                    {
                        "generatedInvoiceUUID": "${UUID.randomUUID()}",
                        "generatedResult": "SUCCEEDED",
                        "uploadedFilePathIssuing": "http://localhost:4443/issuing-dev-bucket/issuing/${issuer.issuerUuid}/issuing-invoice/${issuerInvoiceUUID.value}.pdf"
                    }
                    """.trimIndent().toByteArray(),
        )

      val testRequest =
        """
                {
                    "task": {
                        "httpRequest": {
                            "httpMethod": 1,
                            "url": "/event-handler/issuing/invoices/reflect-issued-result",
                            "headers": {
                                "X-Tenant-Name-Id": "gogo",
                                "X-Cloud-Trace-Context": "$callUUID/0;o=1"
                            },
                            "body": "$bodyEncoded"
                        }
                    }
                }
                """.trimIndent()

      val call =
        handleRequest(HttpMethod.Post, "/event-handler/issuing/invoices/reflect-issued-result") {
          addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
          setBody(testRequest)
        }

      assertEquals(HttpStatusCode.BadRequest, call.response.status())
    }
}
