package issuing.controller

import com.sansan.billone.lib.principal
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import issuing.domain.tenant.TenantNameId
import issuing.query_service.IssuerQueryService


@Resource("/api/issuing/issuer")
class IssuerResource

@Resource("/api/issuing/issuer/{tenantNameId}")
class IssuerResourceWithoutLogin(
  val tenantNameId: String
)

fun Route.issuerRoutes() {
  get<IssuerResource> {
    val principal = call.principal()
    val result = IssuerQueryService.getAll(TenantNameId(principal.tenantNameId))
    call.respond(result)
  }

  get<IssuerResourceWithoutLogin> {
    val issuerUUID = it.tenantNameId
    val result = IssuerQueryService.getAll(TenantNameId(issuerUUID))
    call.respond(result)
  }
}