package recipient.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import recipient.fixture.*
import recipient.module
import recipient.testing.Database
import recipient.testing.testSettings
import java.util.*
import kotlin.test.Test

class InvoiceControllerPutTest {
    companion object {
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)
        private val tenantNameId = "yonyon"

        private lateinit var tenant: TenantTableFixture
        private lateinit var recipient: RecipientTableFixture
        private lateinit var invoice: InvoiceTableFixture

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            database.tearDown()
        }
    }


    @Before
    fun before() {
        println("before")
        database.setUpWithDsl {
            tenant = insertTenant(tenantNameId) associates {
                recipient = insertRecipient() associates {
                    invoice = insertInvoice()
                }
            }
        }
    }

    @Test
    fun `can update invoice`(): Unit = withTestApplication({ module(true, settings) }) {
        // language=json
        val body = """
            {
                "invoiceAmount": 9999,
                "supplierUUID": "${UUID.randomUUID()}",
                "paymentDeadline": "2019-02-01"
            }
        """.trimIndent()
        handleUpdateInvoice(invoice.invoiceUuid, body).run {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
        }

    }


    private fun TestApplicationEngine.handleUpdateInvoice(invoiceUUID: UUID, body: String?) =
        handleRequest(HttpMethod.Put, "/api/recipient/invoice/$invoiceUUID") {
            addHeader("X-App-Tenant-Name-Id", tenant.tenantNameId)
            addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
            addHeader("X-App-Invoice-UUID", invoice.invoiceUuid.toString())
            addHeader("Content-Type", "application/json")
            if (body != null) setBody(body)
        }
}
