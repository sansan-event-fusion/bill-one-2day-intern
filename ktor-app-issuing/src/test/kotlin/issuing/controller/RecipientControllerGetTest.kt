package issuing.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import issuing.domain.tenant.TenantNameId
import issuing.fixture.RecipientTableFixture
import issuing.fixture.TenantTableFixture
import issuing.fixture.insertRecipient
import issuing.fixture.insertTenant
import issuing.module
import issuing.testing.Database
import issuing.testing.testSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.AfterAll
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
                    insertRecipient("受領者1", "recipient@yonyon.com")
                recipient2 =
                    insertRecipient("受領者2", "recipient2@yonyon.com")
                recipient3 =
                    insertRecipient("受領者3", "recipient3@yonyon.com")
            }
        }
    }

    @Test
    fun `should return recipients`(): Unit =
        withTestApplication({ module(true, settings) }) {
            val expected =
                """
                {
                  "row" : [ {
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
                handleRequest(HttpMethod.Get, "/api/issuing/recipients/${tenantNameId.value}") {
                    addHeader("X-App-Tenant-Name-Id", tenantNameId.value)
                },
            ) {
                assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                assertThat(response.content).isEqualTo(expected)
            }
        }
}
