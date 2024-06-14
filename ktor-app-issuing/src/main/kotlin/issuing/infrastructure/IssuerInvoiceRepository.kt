package issuing.infrastructure

import issuing.domain.issuer.IssuerUUID
import issuing.domain.issuer_invoice.*
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId
import org.jdbi.v3.core.Handle
import java.time.OffsetDateTime
import java.util.*

object IssuerInvoiceRepository {
  fun register(
    issuerInvoice: IssuerInvoice,
    handle: Handle,
  ) {
    val sql =
      """
            INSERT INTO issuer_invoice (
                tenant_name_id,
                supplier_tenant_name_id,
                issuer_invoice_uuid,
                issuer_uuid,
                invoice_amount,
                payment_deadline,
                recipient_uuid
            ) VALUES (
                :tenantNameId,
                :supplierTenantNameId,
                :issuerInvoiceUUID,
                :issuerUUID,
                :invoiceAmount,
                :paymentDeadline,
                :recipientUUID
            )
            """.trimIndent()
    handle.createUpdate(sql)
      .bind("tenantNameId", issuerInvoice.tenantNameId.value)
      .bind("supplierTenantNameId", issuerInvoice.supplierTenantNameId.value)
      .bind("issuerInvoiceUUID", issuerInvoice.issuerInvoiceUUID.value)
      .bind("issuerUUID", issuerInvoice.issuerUUID.value)
      .bind("invoiceAmount", issuerInvoice.invoiceAmount.value)
      .bind("paymentDeadline", issuerInvoice.paymentDeadline.value)
      .bind("recipientUUID", issuerInvoice.recipientUUID.value)
      .execute()
  }

  fun getByIssuerUUID(
    tenantNameId: TenantNameId,
    issuerUUID: IssuerUUID,
    handle: Handle,
  ): List<IssuerInvoice?> {
    //language=SQL
    val sql =
      """
            SELECT
                tenant_name_id,
                issuer_uuid,
                invoice_amount,
                recipient_uuid,
                issuer_invoice_uuid
            FROM
                issuer_invoice
            WHERE
                issuer_uuid = :issuerUUID AND tenant_name_id = :tenantNameId
            """.trimIndent()

    val issuerInvoices =
      handle.createQuery(sql)
        .bind("issuerUUID", issuerUUID.value)
        .bind("tenantNameId", tenantNameId.value)
        .mapTo(IssuerInvoiceRow::class.java)
        .map { it.toIssuerInvoice() }
        .toList()

    return issuerInvoices
  }

  fun getOrNullByIssuerInvoiceUUID(
    issuerInvoiceUUID: IssuerInvoiceUUID,
    tenantNameId: TenantNameId,
    handle: Handle,
  ): IssuerInvoice? {
    val sql =
      """
            SELECT
                tenant_name_id,
                supplier_tenant_name_id,
                issuer_uuid,
                invoice_amount,
                payment_deadline,
                recipient_uuid,
                issuer_invoice_uuid,
                uploaded_file_storage_path
            FROM
                issuer_invoice
            WHERE
                tenant_name_id = :tenantNameId 
                AND issuer_invoice_uuid = :issuerInvoiceUUID
            """.trimIndent()

    val row =
      handle.createQuery(sql)
        .bind("issuerInvoiceUUID", issuerInvoiceUUID.value)
        .bind("tenantNameId", tenantNameId.value)
        .mapTo(IssuerInvoiceRow::class.java)
        .singleOrNull() ?: return null
    return IssuerInvoice.of(
      tenantNameId = TenantNameId(row.tenantNameId),
      supplierTenantNameId = TenantNameId(row.supplierTenantNameId),
      issuerUUID = IssuerUUID(row.issuerUUID),
      invoiceAmount = InvoiceAmount(row.invoiceAmount),
      paymentDeadline = PaymentDeadline(OffsetDateTime.now()),
      recipientUUID = RecipientUUID(row.recipientUUID),
      issuerInvoiceUUID = IssuerInvoiceUUID(row.issuerInvoiceUUID),
      uploadedFileStoragePath = UploadedFileStoragePath(row.uploadedFileStoragePath),
    )
  }

  fun reflectIssueResult(
    tenantNameId: TenantNameId,
    issuerInvoice: IssuerInvoice,
    handle: Handle,
  ) {
    val sql =
      """
            UPDATE issuer_invoice
            SET
                uploaded_file_storage_path = :uploadedFileStoragePath
            WHERE
                tenant_name_id = :tenantNameId AND
                issuer_invoice_uuid = :issuerInvoiceUUID
            """.trimIndent()

    handle.createUpdate(sql)
      .bind("tenantNameId", tenantNameId.value)
      .bind("uploadedFileStoragePath", issuerInvoice.uploadedFileStoragePath?.value)
      .bind("issuerInvoiceUUID", issuerInvoice.issuerInvoiceUUID.value)
      .execute()

  }
}

data class IssuerInvoiceRow(
  val tenantNameId: String,
  val supplierTenantNameId: String,
  val issuerUUID: UUID,
  val invoiceAmount: Int,
  val paymentDeadline: String,
  val recipientUUID: UUID,
  val issuerInvoiceUUID: UUID,
  val uploadedFileStoragePath: String?,
) {
  fun toIssuerInvoice(): IssuerInvoice {
    return IssuerInvoice.of(
      tenantNameId = TenantNameId(tenantNameId),
      supplierTenantNameId = TenantNameId(supplierTenantNameId),
      issuerUUID = IssuerUUID(issuerUUID),
      invoiceAmount = InvoiceAmount(invoiceAmount),
      paymentDeadline = PaymentDeadline(OffsetDateTime.now()),
      recipientUUID = RecipientUUID(recipientUUID),
      issuerInvoiceUUID = IssuerInvoiceUUID(issuerInvoiceUUID),
      uploadedFileStoragePath = UploadedFileStoragePath(uploadedFileStoragePath),
    )
  }
}
