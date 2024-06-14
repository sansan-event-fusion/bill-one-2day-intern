package issuing.controller

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import issuing.domain.recipient.RecipientEmail
import issuing.domain.recipient.RecipientName
import issuing.domain.tenant.TenantNameId
import issuing.query_service.RecipientQueryService

@Resource("/api/issuing/recipients/{tenantNameId}")
class RecipientGetLocation(val tenantNameId: String)

fun Route.recipientController() {
  get<RecipientGetLocation> {
    val recipients =
      RecipientQueryService.getByTenantNameId(
        TenantNameId(it.tenantNameId),
      )
    call.respond(recipients)
  }
}

data class RecipientCreateArgs(
  val recipientName: RecipientName,
  val recipientEmail: RecipientEmail,
)
