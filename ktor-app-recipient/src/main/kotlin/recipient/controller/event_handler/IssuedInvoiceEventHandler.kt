package recipient.controller.event_handler

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
import kotlinx.serialization.Serializable
import recipient.application_service.InvoiceRegisterFromIssuingResultService
import recipient.controller.Request
import recipient.domain.invoice.RecipientInvoicePublished
import recipient.domain.tenant.TenantNameId
import java.util.*


@Resource("/event-handler/recipient/invoices/reflect-issued-result")
class IssuedInvoiceEventHandler

fun Route.issuedInvoiceEventHandler() {
    post<IssuedInvoiceEventHandler> {
        val taskRequest = call.receive<TaskRequest>()

        //  BodyのみBase64でエンコードされているのでデコードしてパースする
        val decodeJson = String(Base64.getDecoder().decode(taskRequest.task.httpRequest.body))
        val objectMapper: ObjectMapper = jacksonObjectMapper()

        val request = objectMapper.readValue(decodeJson, RecipientInvoicePublished::class.java)

        val result =
            InvoiceRegisterFromIssuingResultService.register(
                TenantNameId(taskRequest.task.httpRequest.headers?.get("X-Tenant-Name-Id") ?: ""),
                call.getDomainEventContext(),
                request,
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
