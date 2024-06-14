package recipient.application_service

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.DomainEventContext
import recipient.domain.invoice.*
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.infrastructure.InvoiceRepository
import recipient.infrastructure.TenantRepository
import recipient.util.runInTransaction
import java.time.OffsetDateTime

object InvoiceRegisterFromIssuingResultService {
    fun register(
        tenantNameId: TenantNameId,
        domainEventContext: DomainEventContext,
        args: RecipientInvoicePublished,
    ): ApplicationResult {
        return runInTransaction(tenantNameId, domainEventContext) { handle ->
            val issuedTenant = TenantRepository.getOrNull(TenantNameId(args.tenantNameId))
                ?: return@runInTransaction ApplicationResult.Failure("Tenant Not Found")

            val invoice =
                Invoice.fromEvent(
                    tenantNameId = tenantNameId,
                    invoiceUUID = InvoiceUUID(args.generatedInvoiceUUID),
                    recipientUUID = RecipientUUID(args.recipientUUID),
                    registeredBy = RegisteredBy.RECEIVE,
                    invoiceAmount = InvoiceAmount(args.invoiceAmount),
                    supplierUUID = SupplierUUID(issuedTenant.tenantUUID.value),
                    supplierName = SupplierName(issuedTenant.tenantNameId.value),
                    paymentDeadline = PaymentDeadline(OffsetDateTime.parse(args.paymentDeadline).toLocalDate()),
                    registeredAt = RegisteredAt(OffsetDateTime.now())
                )

            InvoiceRepository.save(invoice, handle)
            ApplicationResult.Success
        }
    }
}

