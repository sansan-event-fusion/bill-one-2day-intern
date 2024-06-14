package recipient.query_service

import recipient.domain.tenant.TenantNameId
import recipient.util.withHandle
import java.util.*

object TenantQueryService {
    fun getTenants(tenantNameId: TenantNameId): TenantQueryResult {
        withHandle(tenantNameId) { handle ->
            val tenants = handle.createQuery(TenantRow.all())
                .mapTo(TenantRow::class.java)
                .toSet()

            return TenantQueryResult.fromRow(tenants)
        }
    }

    fun getSuppliers(tenantNameId: TenantNameId): TenantQueryResult {
        withHandle(tenantNameId) { handle ->
            val tenants = handle.createQuery(TenantRow.others())
                .bind("tenantNameId", tenantNameId.value)
                .mapTo(TenantRow::class.java)
                .toSet()
            return TenantQueryResult.fromRow(tenants)
        }
    }
}

data class TenantRow(
    val tenantNameId: String,
    val tenantUUID: UUID,
    val locale: String,
) {
    companion object {
        fun all(): String {
            return """
                SELECT
                    tenant_name_id,
                    tenant_uuid,
                    locale
                FROM
                    tenant
                ORDER BY
                    created_at
            """.trimIndent()
        }

        fun others(): String {
            return """
                SELECT
                    tenant_name_id,
                    tenant_uuid,
                    locale
                FROM
                    tenant
                WHERE
                    tenant_name_id != :tenantNameId
                ORDER BY
                    created_at
            """.trimIndent()
        }
    }
}


data class TenantQueryResult(
    val value: Set<TenantRow>
) {
    companion object {
        fun fromRow(row: Set<TenantRow>): TenantQueryResult {
            return TenantQueryResult(
                value = row
            )
        }
    }
}
