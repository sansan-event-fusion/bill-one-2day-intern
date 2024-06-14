package issuing.infrastructure

import issuing.domain.issuer.Issuer
import issuing.domain.issuer.IssuerEmail
import issuing.domain.issuer.IssuerName
import issuing.domain.issuer.IssuerUUID
import issuing.domain.tenant.TenantNameId
import org.jdbi.v3.core.Handle
import java.util.*

object IssuerRepository {
  fun getOrNull(
    tenantNameId: TenantNameId,
    issuerUUID: IssuerUUID,
    handle: Handle,
  ): Issuer? {
    val sql =
      """
            SELECT
                issuer_uuid,
                issuer_name,
                issuer_email,
                tenant_name_id
            FROM issuer
            WHERE 
                issuer_uuid = :issuerUUID AND
                tenant_name_id = :tenantNameId
            """.trimIndent()

    return handle.createQuery(sql)
      .bind("issuerUUID", issuerUUID.value)
      .bind("tenantNameId", tenantNameId.value)
      .mapTo(IssuerRow::class.java)
      .singleOrNull()
      ?.toIssuer()
  }

  data class IssuerRow(
    val issuer_uuid: UUID,
    val issuer_name: String,
    val issuer_email: String,
    val tenant_name_id: String,
  ) {
    fun toIssuer() =
      Issuer(
        issuerUUID = IssuerUUID(issuer_uuid),
        issuerName = IssuerName(issuer_name),
        issuerEmail = IssuerEmail(issuer_email),
        tenantNameId = TenantNameId(tenant_name_id),
      )
  }
}
