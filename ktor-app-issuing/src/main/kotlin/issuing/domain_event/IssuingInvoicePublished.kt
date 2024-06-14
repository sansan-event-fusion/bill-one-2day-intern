package issuing.domain_event

import java.util.*

data class IssuingInvoicePublished(
  val generatedInvoiceUUID: UUID,
  val generatedResult: String,
  val uploadedFilePathIssuing: String,
)
