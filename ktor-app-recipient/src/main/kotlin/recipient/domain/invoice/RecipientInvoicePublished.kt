package recipient.domain.invoice

import java.util.*

data class RecipientInvoicePublished(
    val generatedInvoiceUUID: UUID,
    val tenantNameId: String,
    val issuerUUID: UUID,
    val issuerName: String,
    val recipientUUID: UUID,
    val invoiceAmount: Int,
    val paymentDeadline: String,
    val generatedResult: String,
    val uploadedFilePathRecipient: String,
)
