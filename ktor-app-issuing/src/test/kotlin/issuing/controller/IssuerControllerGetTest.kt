package issuing.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import issuing.domain.IssuerInvoiceTest.Companion.tenantNameId
import issuing.fixture.IssuerTableFixture
import issuing.fixture.TenantTableFixture
import issuing.fixture.insertIssuer
import issuing.fixture.insertTenant
import issuing.module
import issuing.testing.Database
import issuing.testing.testSettings
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import kotlin.test.Test

class IssuerControllerGetTest {
    companion object {
        // テスト用のセットアップ
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        // テスト用のフィクスチャ(Fixture DSL)
        private lateinit var tenant: TenantTableFixture
        private lateinit var issuer1: IssuerTableFixture
        private lateinit var issuer2: IssuerTableFixture
        private lateinit var issuer3: IssuerTableFixture

        @JvmStatic
        @AfterAll
        fun afterAll() {
            database.tearDown()
        }
    }

    @Before
    fun before() {
        database.setUpWithDsl {
            tenant = insertTenant("yonyon") associates {
                issuer1 = insertIssuer("発行ユーザー1", "issuer1@yonyon.com")
                issuer2 = insertIssuer("発行ユーザー2", "issuer2@yonyon.com")
                issuer3 = insertIssuer("発行ユーザー3", "issuer3@yonyon.com")
            }
        }
    }

    @Test
    fun `should return issuers`(): Unit =
        withTestApplication({ module(true, settings) }) {
            val expected =
                """
                {
                  "issuers" : [ {
                    "tenantNameId" : "${tenant.tenantNameId}",
                    "issuerUUID" : "${issuer1.issuerUuid}",
                    "fullName" : "${issuer1.issuerName}",
                    "email" : "${issuer1.issuerEmail}"
                  }, {
                    "tenantNameId" : "${tenant.tenantNameId}",
                    "issuerUUID" : "${issuer2.issuerUuid}",
                    "fullName" : "${issuer2.issuerName}",
                    "email" : "${issuer2.issuerEmail}"
                  }, {
                    "tenantNameId" : "${tenant.tenantNameId}",
                    "issuerUUID" : "${issuer3.issuerUuid}",
                    "fullName" : "${issuer3.issuerName}",
                    "email" : "${issuer3.issuerEmail}"
                  } ]
                }
                """.trimIndent()
            with(
                handleRequest(HttpMethod.Get, "/api/issuing/issuer/${tenant.tenantNameId}"),
            ) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertThat(response.content).isEqualTo(expected)
            }
        }
}
