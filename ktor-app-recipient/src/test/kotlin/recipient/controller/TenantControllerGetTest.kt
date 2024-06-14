package recipient.controller

import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.jupiter.api.AfterAll
import recipient.fixture.TenantTableFixture
import recipient.fixture.insertTenant
import recipient.module
import recipient.testing.Database
import recipient.testing.testSettings
import java.util.*
import kotlin.test.Test

class TenantControllerGetTest {
    companion object {
        private val yonyon = "yonyon"
        private val gogo = "gogo"
        private val rokuroku = "rokuroku"

        // テスト用のセットアップ
        private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
        private val settings = testSettings(schemaName)
        private val database = Database(settings, schemaName)

        // テスト用のフィクスチャ(Fixture DSL)
        private lateinit var tenant: TenantTableFixture
        private lateinit var tenant2: TenantTableFixture
        private lateinit var tenant3: TenantTableFixture

        @JvmStatic
        @AfterAll
        fun afterAll() {
            database.tearDown()
        }
    }

    @Before
    fun before() {
        database.setUpWithDsl {
            tenant = insertTenant(yonyon)
            tenant2 = insertTenant(gogo)
            tenant3 = insertTenant(rokuroku)
        }
    }

    @Test
    fun `can get all tenant`(): Unit = withTestApplication({ module(true, settings) }) {
        val expected = """
            {
              "value" : [ {
                "tenantNameId" : "${tenant.tenantNameId}",
                "tenantUUID" : "${tenant.tenantUuid}",
                "locale" : "${tenant.locale}"
              }, {
                "tenantNameId" : "${tenant2.tenantNameId}",
                "tenantUUID" : "${tenant2.tenantUuid}",
                "locale" : "${tenant2.locale}"
              }, {
                "tenantNameId" : "${tenant3.tenantNameId}",
                "tenantUUID" : "${tenant3.tenantUuid}",
                "locale" : "${tenant3.locale}"
              } ]
            }
            """.trimIndent()
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/tenant") {
                addHeader("X-App-Tenant-Name-Id", yonyon)
                addHeader("X-App-User-UUID", UUID.randomUUID().toString())
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)

            println(response.content)
            assertThat(response.content).isEqualTo(expected)
        }
    }

    @Test
    fun `can get supplier`(): Unit = withTestApplication({ module(true, settings) }) {
        val expected = """
            {
              "value" : [ {
                "tenantNameId" : "${tenant2.tenantNameId}",
                "tenantUUID" : "${tenant2.tenantUuid}",
                "locale" : "${tenant2.locale}"
              }, {
                "tenantNameId" : "${tenant3.tenantNameId}",
                "tenantUUID" : "${tenant3.tenantUuid}",
                "locale" : "${tenant3.locale}"
              } ]
            }
            """.trimIndent()
        with(
            handleRequest(HttpMethod.Get, "/api/recipient/supplier") {
                addHeader("X-App-User-UUID", UUID.randomUUID().toString())
                addHeader("X-App-Tenant-Name-Id", yonyon)
            }
        ) {
            assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
            assertThat(response.content).isEqualTo(expected)
        }
    }
}
