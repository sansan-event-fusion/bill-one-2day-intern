package recipient.infrastructure

import org.jdbi.v3.core.Handle
import recipient.domain.invoice.Invoice
import recipient.domain.invoice.InvoiceUUID
import recipient.domain.tenant.TenantNameId
import java.time.OffsetDateTime
import java.util.*

object InvoiceRepository {

    fun getOrNull(invoiceUUID: InvoiceUUID, tenantNameId: TenantNameId, handle: Handle): Invoice? {
        val sql = """
                SELECT
                    tenant_name_id,
                    invoice_uuid,
                    recipient_uuid,
                    invoice_amount,
                    supplier_uuid,
                    supplier_name,
                    payment_deadline,
                    registered_by,
                    registered_at
                FROM invoice
                WHERE tenant_name_id = :tenantNameId
                AND invoice_uuid = :invoiceUUID
            """.trimIndent()

        val row = handle.createQuery(sql)
            .bind("tenantNameId", tenantNameId.value)
            .bind("invoiceUUID", invoiceUUID.value)
            .mapTo(InvoiceRow::class.java)
            .singleOrNull() ?: return null
        return Invoice.from(
            row.tenantNameId,
            row.invoiceUUID,
            row.recipientUUID,
            row.invoiceAmount,
            row.supplierUUID,
            row.supplierName,
            row.paymentDeadline,
            row.registeredBy,
            row.registeredAt
        )
    }

    fun save(invoice: Invoice, handle: Handle) {
        //language=postgresql
        val sql = """
                INSERT INTO invoice (
                    tenant_name_id,
                    invoice_uuid,
                    recipient_uuid,
                    invoice_amount,
                    supplier_uuid,
                    supplier_name,
                    payment_deadline,
                    registered_by,
                    registered_at
                ) VALUES (
                    :tenantNameId,
                    :invoiceUUID,
                    :recipientUUID,
                    :invoiceAmount,
                    :supplierUUID,
                    :supplierName,
                    :paymentDeadline,
                    :registeredBy,
                    now()
                ) ON CONFLICT (invoice_uuid) DO UPDATE SET
                    invoice_amount = :invoiceAmount,
                    supplier_uuid = :supplierUUID,
                    supplier_name = :supplierName,
                    payment_deadline = :paymentDeadline;
            """.trimIndent()

        invoice.let {
            handle.createUpdate(sql)
                .bind("tenantNameId", it.tenantNameId.value)
                .bind("invoiceUUID", it.invoiceUUID.value)
                .bind("recipientUUID", it.recipientUUID.value)
                .bind("invoiceAmount", it.invoiceAmount?.value)
                .bind("invoiceAmount", it.invoiceAmount?.value)
                .bind("supplierUUID", it.supplierUUID?.value)
                .bind("supplierName", it.supplierName?.value)
                .bind("paymentDeadline", it.paymentDeadline?.value)
                .bind("registeredBy", it.registeredBy.name)
                .execute()
        }
    }

    fun delete(invoiceUUID: InvoiceUUID, tenantNameId: TenantNameId, handle: Handle) {
        val sql = """
                DELETE FROM invoice
                WHERE tenant_name_id = :tenantNameId
                AND invoice_uuid = :invoiceUUID
            """.trimIndent()

        handle.createUpdate(sql)
            .bind("tenantNameId", tenantNameId.value)
            .bind("invoiceUUID", invoiceUUID.value)
            .execute()
    }
}


data class InvoiceRow(
    val tenantNameId: String,
    val invoiceUUID: UUID,
    val recipientUUID: UUID,
    val invoiceAmount: Int?,
    val supplierUUID: UUID?,
    val supplierName: String?,
    val paymentDeadline: String?,
    val registeredBy: String,
    val registeredAt: OffsetDateTime
)
