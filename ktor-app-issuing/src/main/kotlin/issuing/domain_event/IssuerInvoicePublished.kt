package issuing.domain_event

import issuing.domain.issuer.IssuerUUID
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.recipient.RecipientUUID

data class IssuerInvoicePublished(
  val tenantNameId: String,
  val issuerInvoiceUUID: IssuerInvoiceUUID,
  val issuerUUID: IssuerUUID,
  val recipientUUID: RecipientUUID,
  val issuerName: String,
  val issuerOrganizationName: String,
  val recipientName: String,
  val recipientOrganizationName: String,
  val paymentDueDate: String,
  val taxRate: String,
  val invoiceTaxExcludedAmount: String
) : DomainEvent
