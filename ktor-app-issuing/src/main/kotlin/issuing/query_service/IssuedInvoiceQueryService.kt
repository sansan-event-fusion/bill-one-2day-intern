package issuing.query_service

import issuing.domain.issuer.IssuerUUID
import issuing.domain.tenant.TenantNameId
import issuing.util.withHandle
import java.util.*

object IssuedInvoiceQueryService {
  fun getByIssuerUUID(
    issuerUUID: IssuerUUID,
    tenantNameId: TenantNameId,
  ): IssuerInvoiceQueryResult {
    withHandle(tenantNameId) { handle ->
      // language=SQL
      val sql = """
        SELECT
                ii.tenant_name_id,
                ii.supplier_tenant_name_id,
                ii.issuer_uuid,
                ii.invoice_amount,
                ii.payment_deadline,
                ii.recipient_uuid,
                r.full_name as recipient_name,
                i.issuer_name as issuer_name,
                ii.issuer_invoice_uuid,
                ii.uploaded_file_storage_path as url
            FROM
                issuer_invoice ii
                inner join issuer i on ii.issuer_uuid = i.issuer_uuid
                inner join recipient r on ii.recipient_uuid = r.recipient_uuid
            WHERE
                ii.issuer_uuid = :issuerUUID AND ii.tenant_name_id = :tenantNameId
      """.trimIndent()

      val invoices = handle.createQuery(sql)
        .bind("issuerUUID", issuerUUID.value)
        .bind("tenantNameId", tenantNameId.value)
        .mapTo(IssuerInvoiceRow::class.java)
        .toList()

      return IssuerInvoiceQueryResult(invoices)
    }
  }
}

data class IssuerInvoiceRow(
  val tenantNameId: String,
  val supplierTenantNameId: String,
  val issuerUUID: UUID,
  val issuerName: String,
  val recipientName: String,
  val invoiceAmount: Int,
  val paymentDeadline: String,
  val recipientUUID: UUID,
  val issuerInvoiceUUID: UUID,
  val url: String?,
)

data class IssuerInvoiceQueryResult(
  val rows: List<IssuerInvoiceRow>
)