package issuing.controller.event_handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sansan.billone.lib.ApplicationResult
import com.sansan.billone.lib.getDomainEventContext
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import issuing.application_service.IssuerInvoiceReflectIssuedResultService
import issuing.controller.Request
import issuing.domain.issuer_invoice.IssuerInvoiceUUID
import issuing.domain.tenant.TenantNameId
import issuing.domain_event.IssuingInvoicePublished
import kotlinx.serialization.Serializable
import java.util.*

@Resource("/event-handler/issuing/invoices/reflect-issued-result")
class IssuedInvoiceEventHandlerResource

fun Route.issuedInvoiceEventHandler() {
  post<IssuedInvoiceEventHandlerResource> {
    val taskRequest = call.receive<TaskRequest>()
    //  BodyのみBase64でエンコードされているのでデコードしてパースする
    val decodeJson = String(Base64.getDecoder().decode(taskRequest.task.httpRequest.body))
    val objectMapper: ObjectMapper = jacksonObjectMapper()
    val issuerInvoiceReflectIssuedResultRequest =
      objectMapper.readValue(decodeJson, IssuingInvoicePublished::class.java)

    val result =
      IssuerInvoiceReflectIssuedResultService.reflect(
        TenantNameId(taskRequest.task.httpRequest.headers?.get("X-Tenant-Name-Id") ?: ""),
        call.getDomainEventContext(),
        IssuerInvoiceUUID((issuerInvoiceReflectIssuedResultRequest.generatedInvoiceUUID)),
        issuerInvoiceReflectIssuedResultRequest.uploadedFilePathIssuing,
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
}


@Serializable
data class TaskRequest(
  val task: Request,
)