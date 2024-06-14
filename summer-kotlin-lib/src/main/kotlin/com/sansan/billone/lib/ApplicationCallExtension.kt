package com.sansan.billone.lib

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import java.util.*

fun ApplicationCall.getTraceContext(): TraceContext = TraceContext(
  request.headers["X-Cloud-Trace-Context"] ?: "00000000000000000000000000000000/0"
)

fun ApplicationCall.getCallUUID(): CallUUID = CallUUID(
  UUID.fromString(callId)
)

fun ApplicationCall.getDomainEventContext() = DomainEventContext(getCallUUID(), getTraceContext())
fun ApplicationCall.principal(): AppUserPrincipal {
  val tenantNameId = this.request.headers["X-App-Tenant-Name-Id"]!!
  val userUUID = this.request.headers["X-App-User-UUID"]!!

  return AppUserPrincipal(
    tenantNameId = tenantNameId,
    userUUID = UUID.fromString(userUUID),
  )
}

data class AppUserPrincipal(
  val tenantNameId: String,
  val userUUID: UUID,
)

data class DomainEventContext(
  val callUUID: CallUUID,
  val traceContext: TraceContext
)
