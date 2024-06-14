package issuing.application_service

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.DomainEventContext
import issuing.controller.IssuerInvoiceCreateArgs
import issuing.domain.issuer.IssuerUUID
import issuing.domain.tenant.TenantNameId
import issuing.util.runInTransaction

object IssuerInvoiceRegisterService {
    fun register(
        args: IssuerInvoiceCreateArgs,
        issuerUUID: IssuerUUID,
        tenantNameId: TenantNameId,
        domainEventContext: DomainEventContext,
    ): ApplicationResult {
        return runInTransaction(tenantNameId, domainEventContext) {
            //  TODO: 課題3
            println(args)
            println(issuerUUID)
//      val issuer =
//        IssuerRepository.getOrNull(tenantNameId, issuerUUID, handle)
//          ?: return@runInTransaction ApplicationResult.Failure("Issuer Not Found")
//
//      val recipient =
//        RecipientRepository.getOrNUll(args.recipientUUID, handle)
//          ?: return@runInTransaction ApplicationResult.Failure("Recipient Not Found")

//              IssuerInvoiceRepository.register(issuerInvoice, handle)

            ApplicationResult.Success
        }
    }
}
