package recipient.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import recipient.domain.tenant.TenantNameId
import recipient.fixture.RecipientTableFixture
import recipient.fixture.TenantTableFixture
import recipient.fixture.insertRecipient
import recipient.fixture.insertTenant
import recipient.module
import recipient.testing.Database
import recipient.testing.testSettings
import java.time.OffsetDateTime
import kotlin.test.Test

class RecipientControllerGetTest {
    companion object {
        private val tenantNameId: TenantNameId = TenantNameId("yonyon")

        // テスト用のセットアップ
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        // テスト用のフィクスチャ(Fixture DSL)
        private lateinit var tenant: TenantTableFixture
        private lateinit var recipient: RecipientTableFixture
        private lateinit var recipient2: RecipientTableFixture
        private lateinit var recipient3: RecipientTableFixture

        @JvmStatic
        @AfterAll
        fun afterAll() {
            database.tearDown()
        }
    }

    @Before
    fun before() {
        database.setUpWithDsl {
            tenant = insertTenant(tenantNameId.value) associates {
                recipient =
                    insertRecipient("受領者1", "recipient@yonyon.com", OffsetDateTime.parse("2019-01-01T00:00:00Z"))
                recipient2 =
                    insertRecipient("受領者2", "recipient2@yonyon.com", OffsetDateTime.parse("2019-02-01T00:00:00Z"))
                recipient3 =
                    insertRecipient("受領者3", "recipient3@yonyon.com", OffsetDateTime.parse("2019-03-03T00:00:00Z"))

            }
        }
    }

    @Test
    fun `can get recipients`(): Unit = withTestApplication({ module(true, settings) }) {
        val expected = """
            {
              "recipients" : [ {
                "tenantNameId" : "yonyon",
                "recipientUUID" : "${recipient.recipientUuid}",
                "fullName" : "受領者1",
                "email" : "recipient@yonyon.com"
              }, {
                "tenantNameId" : "yonyon",
                "recipientUUID" : "${recipient2.recipientUuid}",
                "fullName" : "受領者2",
                "email" : "recipient2@yonyon.com"
              }, {
                "tenantNameId" : "yonyon",
                "recipientUUID" : "${recipient3.recipientUuid}",
                "fullName" : "受領者3",
                "email" : "recipient3@yonyon.com"
              } ]
            }
        """.trimIndent()
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/recipients") {
                addHeader("X-App-Tenant-Name-Id", tenantNameId.value)
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())

            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(expected)
        }
    }

    @Test
    fun `can get recipient`(): Unit = withTestApplication({ module(true, settings) }) {
        val expected = """
                    {
                      "tenantNameId" : "yonyon",
                      "recipientUUID" : "${recipient.recipientUuid}",
                      "fullName" : "受領者1",
                      "email" : "recipient@yonyon.com"
                    }
                """.trimIndent()
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/${recipient.recipientUuid}") {
                addHeader("X-App-User-UUID", recipient.recipientUuid.toString())
                addHeader("X-App-Tenant-Name-Id", tenantNameId.value)
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(expected)
        }
    }
}
