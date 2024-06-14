package recipient.infrastructure

import org.jdbi.v3.core.Handle
import recipient.domain.invoice.SupplierUUID
import recipient.domain.tenant.Tenant
import recipient.domain.tenant.TenantLocale
import recipient.domain.tenant.TenantNameId
import recipient.domain.tenant.TenantUUID
import recipient.util.withHandle
import java.util.*

object TenantRepository {
    fun getOrNull(tenantNameId: TenantNameId): Tenant? {
        val sql = """
            SELECT
                tenant_name_id,
                tenant_uuid,
                locale
            FROM
                tenant
            WHERE
                tenant_name_id = :tenantNameId
        """.trimIndent()

        withHandle(tenantNameId) { handle ->
            val result = handle.createQuery(sql)
                .bind("tenantNameId", tenantNameId.value)
                .mapTo(TenantRow::class.java)
                .singleOrNull()
            return result?.let {
                Tenant(
                    tenantNameId = TenantNameId(it.tenantNameId),
                    tenantUUID = TenantUUID(it.tenantUUID),
                    tenantLocale = TenantLocale.of(it.locale)
                )
            }
        }
    }

    fun getSupplier(supplierUUID: SupplierUUID, handle: Handle): Tenant? {
        val sql = """
            SELECT
                tenant_name_id,
                tenant_uuid,
                locale
            FROM
                tenant
            WHERE
                tenant_uuid = :tenantUUID
        """.trimIndent()

        val result = handle.createQuery(sql)
            .bind("tenantUUID", supplierUUID.value)
            .mapTo(TenantRow::class.java)
            .singleOrNull()
        return result?.let {
            Tenant(
                tenantNameId = TenantNameId(it.tenantNameId),
                tenantUUID = TenantUUID(it.tenantUUID),
                tenantLocale = TenantLocale.of(it.locale)
            )
        }
    }
}

data class TenantRow(
    val tenantNameId: String,
    val tenantUUID: UUID,
    val locale: String
)
