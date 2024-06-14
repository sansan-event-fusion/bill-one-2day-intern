package issuing.controller

import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.getDomainEventContext
import com.sansan.billone.lib.principal
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import issuing.application_service.IssuerInvoiceRegisterService
import issuing.domain.issuer.IssuerUUID
import issuing.domain.issuer_invoice.InvoiceAmount
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.issuer_invoice.PaymentDeadline
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId
import issuing.query_service.IssuedInvoiceQueryService
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.*


@Resource("/api/issuing/invoices")
class IssuerInvoiceResource

@Resource("/api/issuing/invoices/register")
class IssuerInvoiceRegisterResource

@Resource("/api/issuing/invoices/{issuerUUID}")
class IssuerInvoiceGetResource(
  @Contextual val issuerUUID: UUID,
)


fun Route.issuerInvoiceController() {
  get<IssuerInvoiceResource> {
    val principal = call.principal()
    val result = IssuedInvoiceQueryService.getByIssuerUUID(
      IssuerUUID(principal.userUUID),
      TenantNameId(principal.tenantNameId),
    )
    call.respond(result)
  }
  post<IssuerInvoiceRegisterResource> {
    val principal = call.principal()
    val issuerInvoiceCreateRequest = call.receive<IssuerInvoiceCreateRequest>()
    val domainEventContext = call.getDomainEventContext()

    val result =
      IssuerInvoiceRegisterService.register(
        issuerInvoiceCreateRequest.toArgs(),
        IssuerUUID(principal.userUUID),
        TenantNameId(principal.tenantNameId),
        domainEventContext,
      )

    when (result) {
      is ApplicationResult.Success -> {
        call.respond(HttpStatusCode.OK)
      }

      is ApplicationResult.Failure -> {
        call.respond(HttpStatusCode.BadRequest)
      }
    }
  }
  get<IssuerInvoiceGetResource> {
    val issuerInvoices =
      IssuedInvoiceQueryService.getByIssuerUUID(
        IssuerUUID(it.issuerUUID),
        TenantNameId(call.principal().tenantNameId),
      )
    call.respond(issuerInvoices)
  }


}

data class IssuerInvoiceCreateRequest(
  val recipientUUID: UUID,
  val paymentDeadline: String,
  val invoiceAmount: Int,
) {
  fun toArgs() =
    IssuerInvoiceCreateArgs(
      issuerInvoiceUUID = IssuerInvoiceUUID.of(),
      recipientUUID = RecipientUUID(recipientUUID),
      paymentDeadline = PaymentDeadline(OffsetDateTime.parse(paymentDeadline)),
      invoiceAmount = InvoiceAmount(invoiceAmount),
    )
}

data class IssuerInvoiceCreateArgs(
  val issuerInvoiceUUID: IssuerInvoiceUUID,
  val recipientUUID: RecipientUUID,
  val paymentDeadline: PaymentDeadline,
  val invoiceAmount: InvoiceAmount,
)

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

