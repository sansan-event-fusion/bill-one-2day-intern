package recipient.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import recipient.fixture.*
import recipient.module
import recipient.testing.Database
import recipient.testing.testSettings
import kotlin.test.Test

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InvoiceControllerGetTest {
    companion object {
        // テスト用のセットアップ
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        // テスト用のフィクスチャ(Fixture DSL)
        private lateinit var tenant: TenantTableFixture
        private lateinit var recipient: RecipientTableFixture
        private lateinit var invoice: InvoiceTableFixture

        @JvmStatic
        @AfterAll
        fun afterAll(): Unit {
            database.tearDown()
        }
    }


    @Before
    fun before() {
        database.setUpWithDsl {
            tenant = insertTenant("yonyon") associates {
                recipient = insertRecipient() associates {
                    invoice = insertInvoice()
                }
            }
        }
    }

    @Test
    fun `can get invoice`(): Unit = withTestApplication({ module(true, settings) }) {
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/invoice/${invoice.invoiceUuid}") {
                addHeader("X-App-Tenant-Name-Id", tenant.tenantNameId)
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            val invoiceURL = ObjectMapper().readTree(response.content).get("url").asText()
            assertThat(invoiceURL).isEqualTo("http://localhost:4443/recipient-test-bucket/recipient/${recipient.recipientUuid}/recipient-invoices/${invoice.invoiceUuid}.pdf")

        }
    }

    @Test
    fun `can get all invoice`(): Unit = withTestApplication({ module(true, settings) }) {
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/invoices") {
                addHeader("X-App-Tenant-Name-Id", tenant.tenantNameId)
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
        }
    }
}
