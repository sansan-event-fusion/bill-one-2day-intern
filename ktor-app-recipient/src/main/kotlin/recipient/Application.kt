package recipient

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.Level
import recipient.controller.domainEventBroker
import recipient.controller.event_handler.issuedInvoiceEventHandler
import recipient.controller.invoiceController
import recipient.controller.recipientController
import recipient.controller.tenantController
import recipient.util.KtorResourceSerializerModule
import java.util.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
fun Application.module(
    testing: Boolean = false,
    settingsTest: Settings = settingsFromEnv(),
) {
    settings = settingsTest
    install(Resources) {
        serializersModule = KtorResourceSerializerModule
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(CallLogging) {
        level = Level.INFO
    }
    install(CallId) {
        generate { UUID.randomUUID().toString() }
    }
    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowMethods)
        allowHeader("X-Cloud-Trace-Context")
        allowHeader("X-App-Tenant-Name-Id")
        allowHeader("X-App-User-UUID")
        allowHeader("X-App-Request")
    }
    routing {
        invoiceController()
        recipientController()
        tenantController()
        domainEventBroker()
        issuedInvoiceEventHandler()
        get("/") {
            call.respondText("bill-one-1day-intern kotlin-app-recipient is up!")
        }
    }
}

// 初めてその値が必要になった(参照された)時点で初期化される。(lateinit, by lazy共通)

lateinit var settings: Settings
    private set
