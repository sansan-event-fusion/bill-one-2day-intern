package issuing.controller

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.CloudTasksSettings
import com.google.cloud.tasks.v2.Task
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.*
import issuing.domain.tenant.TenantNameId
import issuing.domain_event.DomainEventRow
import issuing.fixture.*
import issuing.infrastructure.IssuerInvoiceRow
import issuing.module
import issuing.testing.Database
import issuing.testing.testSettings
import issuing.util.withHandle
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.core.kotlin.mapTo
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import java.time.OffsetDateTime
import java.util.*
import java.util.UUID.randomUUID
import kotlin.test.Test

class IssuerInvoiceControllerRegisterTest {
    private val callUUID = UUID.randomUUID()

    companion object {
        // setup for test
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        private lateinit var issuerTenant: TenantTableFixture
        private lateinit var issuer: IssuerTableFixture

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
            issuerTenant = insertTenant("yonyon") associates {
                issuer = insertIssuer("発行太郎", "issuer@email.com")
            }
            recipientTenant = insertTenant("gogo") associates {
                recipient = insertRecipient("受領者1", "recipient@email.com")
            }
        }

        mockkStatic(CloudTasksClient::class)
        every { CloudTasksClient.create(any<CloudTasksSettings>()).close() } just Runs
        every {
            CloudTasksClient.create(any<CloudTasksSettings>()).createTask(any<String>(), any())
        } returns Task.newBuilder().build()
    }

    @Test
    fun `should register issuer invoice from parameter`(): Unit =
        withTestApplication({ module(true, settings) }) {
            val paymentDeadline = OffsetDateTime.now().toString()
            with(
                handleRequest(HttpMethod.Post, "/api/issuing/invoices/register") {
                    addHeader("Content-Type", "application/json")
                    addHeader("X-App-Tenant-Name-Id", issuerTenant.tenantNameId)
                    addHeader("X-App-User-UUID", issuer.issuerUuid.toString())
                    addHeader("X-Cloud-Trace-Context", "$callUUID/0;o=1")
                    setBody(
                        """
                        {
                            "recipientUUID": "${recipient.recipientUuid}",
                            "paymentDeadline": "$paymentDeadline",
                            "invoiceAmount": "1000"
                        }
                        """.trimIndent(),
                    )
                },
            ) {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                withHandle(tenantNameId = TenantNameId("yonyon")) { handle ->
                    val issuerInvoiceRow =
                        handle.createQuery("SELECT * FROM issuer_invoice")
                            .mapTo<IssuerInvoiceRow>().one()

                    assertThat(issuerInvoiceRow.uploadedFileStoragePath).isEqualTo(null)
                    assertThat(issuerInvoiceRow.invoiceAmount).isEqualTo(1000)
                    assertThat(issuerInvoiceRow.recipientUUID).isEqualTo(recipient.recipientUuid)
                    // add domainEvent test
                    val domainEventRow =
                        handle
                            .createQuery("SELECT * FROM domain_event")
                            .mapTo<DomainEventRow>()
                            .one()
                    assertThat(domainEventRow.domainEventName).isEqualTo("issuing.domain_event.IssuerInvoiceRegistered")

                }
            }
        }

    @Test
    fun `should return 400 invalid recipientUUID`(): Unit =
        withTestApplication({ module(true, settings) }) {
            val paymentDeadline = OffsetDateTime.now().toString()
            with(
                handleRequest(HttpMethod.Post, "/api/issuing/invoices/register") {
                    addHeader("Content-Type", "application/json")
                    addHeader("X-App-Tenant-Name-Id", issuerTenant.tenantNameId)
                    addHeader("X-App-User-UUID", issuer.issuerUuid.toString())
                    addHeader("X-Cloud-Trace-Context", "$callUUID/0;o=1")
                    setBody(
                        """
                        {
                            "recipientUUID": "${randomUUID()}",
                            "paymentDeadline": "$paymentDeadline",
                            "invoiceAmount": "1000"
                        }
                        """.trimIndent(),
                    )
                },
            ) {
                assertThat(response.status()).isEqualTo(HttpStatusCode.BadRequest)
                verify(exactly = 0) {
                    CloudTasksClient.create(any<CloudTasksSettings>())
                        .createTask(any<String>(), any())
                } // ドメインイベントのタスクが作られていない
            }
        }
}
