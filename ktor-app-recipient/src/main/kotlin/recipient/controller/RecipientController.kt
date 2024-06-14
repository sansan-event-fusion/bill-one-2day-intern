package recipient.controller

import com.sansan.billone.lib.principal
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.query_service.RecipientQueryService
import java.util.*

@Resource("/api/recipient/recipients")
class RecipientsResource

@Resource("/api/recipient/recipients/{tenantNameId}")
class RecipientsResourceWithoutLogin(
    @Contextual val tenantNameId: String,
)

@Resource("/api/recipient/{recipientUUID}")
class RecipientResource(
    @Contextual val recipientUUID: UUID,
)

fun Route.recipientController() {
    get<RecipientsResource> {
        val principal = call.principal()
        // TODO: 課題1
    }

    get<RecipientsResourceWithoutLogin> {
        val tenantNameId = it.tenantNameId
        val result = RecipientQueryService.getRecipients(TenantNameId(tenantNameId))
        call.respond(result)
    }

    get<RecipientResource> {
        val recipientUUID = RecipientUUID(it.recipientUUID)
        val principal = call.principal()
        val result = RecipientQueryService.getRecipientOrNull(TenantNameId(principal.tenantNameId), recipientUUID)
        when (result) {
            null -> call.respond(HttpStatusCode.NotFound, "recipient not found")
            else -> call.respond(result)
        }
    }
}
