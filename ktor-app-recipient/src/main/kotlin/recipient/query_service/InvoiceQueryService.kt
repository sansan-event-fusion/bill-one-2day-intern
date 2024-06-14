package recipient.query_service

import com.sansan.billone.lib.DomainEventContext
import org.jdbi.v3.core.Handle
import recipient.domain.invoice.InvoiceUUID
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.infrastructure.RecipientInvoiceStorage.getUrl
import recipient.util.runInTransaction
import recipient.util.withHandle
import java.net.URL
import java.util.*

object InvoiceQueryService {
    fun getInvoiceByUUID(
        invoiceUUID: InvoiceUUID,
        tenantNameId: TenantNameId,
        domainEventContext: DomainEventContext,
    ): InvoiceQueryResult? {
        // language=postgresql
        val sql =
            """
            SELECT
                i.invoice_uuid,
                i.tenant_name_id,
                i.recipient_uuid,
                r.full_name as recipient_name,
                i.invoice_amount,
                i.supplier_uuid,
                i.supplier_name,
                i.payment_deadline,
                i.registered_by,
                i.registered_at
            FROM
                invoice i
            INNER JOIN recipient r ON i.recipient_uuid = r.recipient_uuid
            WHERE
                i.tenant_name_id = :tenantNameId AND
                i.invoice_uuid = :invoiceUUID
            """.trimIndent()
        return runInTransaction(tenantNameId, domainEventContext) { handle ->
            val invoice =
                handle.createQuery(sql)
                    .bind("tenantNameId", tenantNameId.value)
                    .bind("invoiceUUID", invoiceUUID.value)
                    .mapTo(InvoiceRow::class.java)
                    .singleOrNull()

            invoice?.let { InvoiceQueryResult.fromRow(it) }
        }
    }

    fun getByRecipientUUID(
        tenantNameId: TenantNameId,
        recipientUUID: RecipientUUID,
    ): List<InvoiceQueryResult> {
        // TODO: 課題2
        // language=postgresql
        val sql =
            """
            """.trimIndent()

        withHandle(tenantNameId) { handle ->
            val result =
                handle.createQuery(sql)
                    .mapTo(InvoiceRow::class.java)
                    .list()
            return result.map { InvoiceQueryResult.fromRow(it) }
        }
    }

    fun getByTenantNameId(
        tenantNameId: TenantNameId,
        handle: Handle,
    ): List<InvoiceRow> {
        val sql =
            """
            SELECT
                invoice_uuid,
                tenant_name_id,
                recipient_uuid,
                invoice_amount,
                supplier_uuid,
                supplier_name,
                payment_deadline,
                registered_by,
                registered_at
            FROM
                invoice
            WHERE
                tenant_name_id = :tenantNameId
            """.trimIndent()

        return handle.createQuery(sql)
            .bind("tenantNameId", tenantNameId.value)
            .mapTo(InvoiceRow::class.java)
            .list()
    }
}

data class InvoiceRow(
    val tenantNameId: String,
    val invoiceUUID: UUID,
    val recipientUUID: UUID,
    val recipientName: String,
    val invoiceAmount: Int?,
    val supplierUUID: UUID?,
    val supplierName: String?,
    val paymentDeadline: String?,
    val registeredBy: String,
    val registeredAt: String,
)

data class InvoiceQueryResult(
    val tenantNameId: String,
    val invoiceUUID: UUID,
    val recipientUUID: UUID,
    val recipientName: String,
    val invoiceAmount: Int?,
    val supplierUUID: UUID?,
    val supplierName: String?,
    val paymentDeadline: String?,
    val registeredBy: String,
    val registeredAt: String,
    val url: URL,
) {
    companion object {
        fun fromRow(row: InvoiceRow): InvoiceQueryResult {
            return InvoiceQueryResult(
                invoiceUUID = row.invoiceUUID,
                tenantNameId = row.tenantNameId,
                recipientUUID = row.recipientUUID,
                recipientName = row.recipientName,
                invoiceAmount = row.invoiceAmount,
                supplierUUID = row.supplierUUID,
                supplierName = row.supplierName,
                paymentDeadline = row.paymentDeadline,
                registeredBy = row.registeredBy,
                registeredAt = row.registeredAt,
                url = getUrl(RecipientUUID(row.recipientUUID), InvoiceUUID(row.invoiceUUID)),
            )
        }
    }
}
