package issuing.application_service

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.DomainEventContext
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.issuer_invoice.UploadedFileStoragePath
import issuing.domain.tenant.TenantNameId
import issuing.infrastructure.IssuerInvoiceRepository
import issuing.util.runInTransaction

object IssuerInvoiceReflectIssuedResultService {
  fun reflect(
    tenantNameId: TenantNameId,
    domainEventContext: DomainEventContext,
    issuerInvoiceUUID: IssuerInvoiceUUID,
    uploadedFileStoragePath: String,
  ): ApplicationResult {
    return runInTransaction(tenantNameId, domainEventContext) { handle ->
      val issuerInvoice =
        IssuerInvoiceRepository.getOrNullByIssuerInvoiceUUID(
          issuerInvoiceUUID,
          tenantNameId,
          handle,
        ) ?: return@runInTransaction ApplicationResult.Failure("IssuerInvoice not found")

      val updatedIssuerInvoice = issuerInvoice.reflectIssueResult(UploadedFileStoragePath(uploadedFileStoragePath))

      IssuerInvoiceRepository.reflectIssueResult(tenantNameId, updatedIssuerInvoice, handle)

      ApplicationResult.Success
    }
  }
}
