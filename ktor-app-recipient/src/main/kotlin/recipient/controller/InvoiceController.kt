package recipient.controller

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.getDomainEventContext
import com.sansan.billone.lib.principal
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import recipient.application_service.InvoiceDeleteService
import recipient.application_service.InvoiceUpdateArgs
import recipient.application_service.InvoiceUpdateService
import recipient.application_service.InvoiceUploadService
import recipient.domain.invoice.InvoiceAmount
import recipient.domain.invoice.InvoiceUUID
import recipient.domain.invoice.PaymentDeadline
import recipient.domain.invoice.SupplierUUID
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.query_service.InvoiceQueryService
import java.time.LocalDate
import java.util.*

@Resource("/api/recipient/invoices")
class InvoicesResource

@Resource("/api/recipient/invoice/{invoiceUUID}")
class InvoiceResource(
    @Contextual val invoiceUUID: UUID,
)

@Resource("/api/recipient/invoice/upload")
class InvoiceUploadResource

fun Route.invoiceController() {
    get<InvoicesResource> {
        val principal = call.principal()

        val result =
            InvoiceQueryService.getByRecipientUUID(
                TenantNameId(principal.tenantNameId),
                RecipientUUID(principal.userUUID),
            )
        call.respond(result)
    }

    get<InvoiceResource> {
        val invoiceUUID = InvoiceUUID(it.invoiceUUID)
        val principal = call.principal()
        val result =
            InvoiceQueryService.getInvoiceByUUID(
                invoiceUUID,
                TenantNameId(principal.tenantNameId),
                call.getDomainEventContext(),
            )
        call.respond(result ?: HttpStatusCode.NotFound)
    }

    put<InvoiceResource> {
        val invoiceUUID = it.invoiceUUID
        val principal = call.principal()
        val body = call.receive<InvoiceUpdateRequest>()
        val domainEventContext = call.getDomainEventContext()

        val args =
            body.toArgs(
                InvoiceUUID(invoiceUUID),
                RecipientUUID(principal.userUUID),
            )

        when (
            val result =
                InvoiceUpdateService.update(
                    args,
                    TenantNameId(principal.tenantNameId),
                    domainEventContext,
                )
        ) {
            is ApplicationResult.Success -> {
                call.respond(HttpStatusCode.OK)
            }

            is ApplicationResult.Failure -> {
                call.respond(HttpStatusCode.BadRequest, result.reason)
            }
        }
    }


    delete<InvoiceResource> {
        val invoiceUUID = InvoiceUUID(it.invoiceUUID)
        val principal = call.principal()

        when (
            val result =
                InvoiceDeleteService.delete(
                    invoiceUUID,
                    TenantNameId(principal.tenantNameId),
                    call.getDomainEventContext(),
                )
        ) {
            is ApplicationResult.Success -> {
                call.respond(HttpStatusCode.OK)
            }

            is ApplicationResult.Failure -> {
                call.respond(HttpStatusCode.BadRequest, result.reason)
            }
        }
    }

    post<InvoiceUploadResource> {
        val multipart = call.receiveMultipart()
        val principal = call.principal()
        val domainEventContext = call.getDomainEventContext()

        val recipientUUID = RecipientUUID(principal.userUUID)
        when (
            val result =
                InvoiceUploadService.upload(
                    TenantNameId(principal.tenantNameId),
                    recipientUUID,
                    multipart,
                    domainEventContext,
                )
        ) {
            is ApplicationResult.Success -> {
                call.respond(HttpStatusCode.Created)
            }

            is ApplicationResult.Failure -> {
                call.respond(HttpStatusCode.BadRequest, result.reason)
            }

        }
    }
}

data class InvoiceUpdateRequest(
    val invoiceAmount: Int?,
    val supplierUUID: UUID?,
    val paymentDeadline: String?,
) {
    fun toArgs(
        invoiceUUID: InvoiceUUID,
        userUUID: RecipientUUID,
    ): InvoiceUpdateArgs {
        return InvoiceUpdateArgs(
            invoiceUUID,
            userUUID,
            invoiceAmount?.let { InvoiceAmount(it) },
            supplierUUID?.let { SupplierUUID(it) },
            paymentDeadline?.let { PaymentDeadline(LocalDate.parse(it)) },
        )
    }
}

@Serializable
data class HttpRequest(
    val httpMethod: Int,
    val url: String?,
    val headers: Map<String, String>?,
    val body: String?,
)

@Serializable
data class Request(
    val httpRequest: HttpRequest,
)

