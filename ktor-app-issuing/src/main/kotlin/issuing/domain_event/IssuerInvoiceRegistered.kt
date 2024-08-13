package issuing.domain_event

import issuing.domain.issuer.IssuerUUID
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId

data class IssuerInvoiceRegistered(
    val tenantNameId: String,
    val recipientTenantNameId: TenantNameId,
    val issuerInvoiceUUID: IssuerInvoiceUUID,
    val issuerUUID: IssuerUUID,
    val recipientUUID: RecipientUUID,
    val issuerName: String,
    val issuerEmail: String,
    val recipientName: String,
    val recipientEmail: String,
    val paymentDueDate: String,
    val invoiceAmount: String,
) : DomainEvent
