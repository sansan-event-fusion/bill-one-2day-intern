package recipient.controller

import com.sansan.billone.lib.principal
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import recipient.domain.tenant.TenantNameId
import recipient.query_service.TenantQueryService

@Resource("/api/recipient/tenant")
class TenantResource

@Resource("/api/recipient/supplier")
class SupplierResource

fun Route.tenantController() {
    get<TenantResource> {
        val principal = call.principal()
        val result = TenantQueryService.getTenants(TenantNameId(principal.tenantNameId))
        call.respond(result)
    }

    get<SupplierResource> {
        val principal = call.principal()
        val result = TenantQueryService.getSuppliers(TenantNameId(principal.tenantNameId))
        call.respond(result)
    }
}
