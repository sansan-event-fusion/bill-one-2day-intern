package issuing.query_service

import issuing.domain.tenant.TenantNameId
import issuing.util.withHandle
import java.util.*

object RecipientQueryService {

  fun getByTenantNameId(tenantNameId: TenantNameId): RecipientQueryResult {
    val sql =
      """
            SELECT
                tenant_name_id,
                recipient_uuid,
                full_name,
                email
            FROM recipient
            WHERE tenant_name_id = :tenantNameId
            """.trimIndent()

    withHandle(tenantNameId) { handle ->
      val recipients =
        handle.createQuery(sql)
          .bind("tenantNameId", tenantNameId.value)
          .mapTo(RecipientRow::class.java)
          .toList()
      return RecipientQueryResult(recipients)
    }
  }
}

data class RecipientQueryResult(
  val row: List<RecipientRow>
)

data class RecipientRow(
  val tenantNameId: String,
  val recipientUUID: UUID,
  val fullName: String,
  val email: String,
)

