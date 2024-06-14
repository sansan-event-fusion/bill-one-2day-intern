package recipient.application_service

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.DomainEventContext
import recipient.domain.invoice.InvoiceUUID
import recipient.domain.tenant.TenantNameId
import recipient.infrastructure.InvoiceRepository
import recipient.util.runInTransaction

object InvoiceDeleteService {
    fun delete(
        invoiceUUID: InvoiceUUID,
        tenantNameId: TenantNameId,
        domainEventContext: DomainEventContext
    ): ApplicationResult {
        return runInTransaction(tenantNameId, domainEventContext) { handle ->
            val invoice = InvoiceRepository.getOrNull(invoiceUUID, tenantNameId, handle)
                ?: return@runInTransaction ApplicationResult.Failure("Invoice not found")
            InvoiceRepository.delete(invoice.invoiceUUID, tenantNameId, handle)

            return@runInTransaction ApplicationResult.Success
        }
    }
}
