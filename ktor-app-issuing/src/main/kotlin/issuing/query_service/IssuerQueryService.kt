package issuing.query_service

import issuing.domain.tenant.TenantNameId
import issuing.util.withHandle
import java.util.*

object IssuerQueryService {
  fun getAll(
    tenantNameId: TenantNameId,
  ): IssuerQueryResult {
    return withHandle(tenantNameId) { handle ->
      val issuerRows = handle.createQuery(IssuerRow.getAll())
        .bind("tenantNameId", tenantNameId.value)
        .mapTo(IssuerRow::class.java)
        .list()

      IssuerQueryResult.fromRow(issuerRows)
    }
  }
}


data class IssuerRow(
  val tenantNameId: String,
  val issuerUuid: UUID,
  val issuerName: String,
  val issuerEmail: String,
) {
  companion object {
    fun getAll(): String {
      return """
        SELECT
          tenant_name_id,
          issuer_uuid,
          issuer_name,
          issuer_email
        FROM
          issuer
        WHERE tenant_name_id = :tenantNameId
      """.trimIndent()
    }
  }
}

data class IssuerQueryResult(
  val issuers: Set<ResultRow>,
) {
  companion object {
    fun fromRow(issuerRows: List<IssuerRow>): IssuerQueryResult {
      return IssuerQueryResult(issuerRows.map { ResultRow.fromRow(it) }.toSet())
    }
  }
}


data class ResultRow(
  val tenantNameId: String,
  val issuerUUID: UUID,
  val fullName: String,
  val email: String,
) {
  companion object {
    fun fromRow(issuerRow: IssuerRow): ResultRow {
      return ResultRow(
        issuerRow.tenantNameId,
        issuerRow.issuerUuid,
        issuerRow.issuerName,
        issuerRow.issuerEmail,
      )
    }
  }

}