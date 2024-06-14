package recipient.controller

import com.sansan.billone.lib.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import recipient.domain.tenant.TenantNameId
import recipient.domain_event.Broker
import recipient.domain_event.DomainEventRepository
import recipient.util.runInTransaction
import java.util.*

@Resource("/domain-event-broker/{callUUID}")
class DomainEventBrokerLocation(val callUUID: String)

fun Route.domainEventBroker() {
    post<DomainEventBrokerLocation> {
        val targetCallUUID = CallUUID(UUID.fromString(it.callUUID))
        val tenantNameId = TenantNameId("yonyon")
        deployDomainEvents(tenantNameId, targetCallUUID, call.getTraceContext(), call.getDomainEventContext())

        call.respond(HttpStatusCode.OK)
    }
}

fun deployDomainEvents(
    tenantNameId: TenantNameId,
    targetCallUUID: CallUUID,
    traceContext: TraceContext,
    domainEventContext: DomainEventContext,
) {
    runInTransaction(tenantNameId, domainEventContext) { handle ->

        val domainEventRepository = DomainEventRepository()

        val domainEvents = domainEventRepository.getByCallUUID(targetCallUUID, handle)

        domainEvents.forEach { domainEventRow ->

            if (domainEventRow.deployed) {
                return@forEach
            }

            println("domainEventRow.domainEventName: ${domainEventRow.domainEventName}")
            val subscribers =
                Broker.pubsub[domainEventRow.domainEventName]
                    ?: throw IllegalStateException("存在しないドメインイベントのキーが指定されました。 ${domainEventRow.domainEventName}")
            subscribers.forEach { subscriber -> subscriber.notify(domainEventRow.message, traceContext) }
        }

        domainEventRepository.markAsDeployed(targetCallUUID, handle)
    }
}
