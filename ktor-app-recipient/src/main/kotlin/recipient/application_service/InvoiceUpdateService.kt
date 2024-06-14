package recipient.application_service

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.DomainEventContext
import recipient.domain.invoice.*
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.infrastructure.InvoiceRepository
import recipient.infrastructure.TenantRepository
import recipient.util.runInTransaction
import java.time.LocalDate
import java.util.*

object InvoiceUpdateService {
    fun update(
        args: InvoiceUpdateArgs,
        tenantNameId: TenantNameId,
        domainEventContext: DomainEventContext
    ): ApplicationResult {
        runInTransaction(tenantNameId, domainEventContext) { handle ->
            val invoice = InvoiceRepository.getOrNull(args.invoiceUUID, tenantNameId, handle)
                ?: return@runInTransaction ApplicationResult.Failure("Invoice not found")

            val supplierTenant = args.supplierUUID?.let { TenantRepository.getSupplier(it, handle) }
            val supplierName = supplierTenant?.tenantNameId?.value?.let { SupplierName(it) }

            val updatedInvoice = invoice.update(
                invoiceAmount = args.invoiceAmount,
                supplierUUID = args.supplierUUID,
                supplierName = supplierName,
                paymentDeadline = args.paymentDeadline
            )
            InvoiceRepository.save(updatedInvoice, handle)
        }

        return ApplicationResult.Success
    }
}

data class InvoiceUpdateArgs(
    val invoiceUUID: InvoiceUUID,
    val recipientUUID: RecipientUUID,
    val invoiceAmount: InvoiceAmount?,
    val supplierUUID: SupplierUUID?,
    val paymentDeadline: PaymentDeadline?,
) {
    companion object {
        fun of(
            invoiceUUID: UUID,
            recipientUUID: UUID,
            invoiceAmount: Int,
            supplierUUID: UUID,
            paymentDeadline: LocalDate
        ) = InvoiceUpdateArgs(
            InvoiceUUID(invoiceUUID),
            RecipientUUID(recipientUUID),
            InvoiceAmount(invoiceAmount),
            SupplierUUID(supplierUUID),
            PaymentDeadline(paymentDeadline)
        )
    }
}
