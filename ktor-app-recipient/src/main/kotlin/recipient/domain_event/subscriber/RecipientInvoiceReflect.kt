package recipient.domain_event.subscriber

import com.google.cloud.tasks.v2.*
import com.google.cloud.tasks.v2.HttpMethod
import com.google.protobuf.ByteString
import com.sansan.billone.lib.TraceContext
import io.ktor.http.*
import org.slf4j.LoggerFactory
import recipient.domain_event.Subscriber
import recipient.domain_event.buildCloudTasksSettings
import recipient.settings
import java.nio.charset.StandardCharsets

class RecipientInvoiceReflect : Subscriber {
    private val logger = LoggerFactory.getLogger(RecipientInvoiceReflect::class.java)

    override fun notify(
        message: String,
        traceContext: TraceContext,
    ) {
        val oidcTokenBuilder = OidcToken.newBuilder().setServiceAccountEmail(settings.serviceAccount)

        val task =
            Task.newBuilder()
                .setHttpRequest(
                    HttpRequest.newBuilder()
                        .putHeaders("X-Cloud-Trace-Context", traceContext.value)
                        .putHeaders(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        .setBody(ByteString.copyFrom(message, StandardCharsets.UTF_8))
                        .setUrl("${settings.invoiceAPIUrl}/event-handler/recipient/invoices/reflect-issued-result")
                        .setOidcToken(oidcTokenBuilder)
                        .setHttpMethod(HttpMethod.POST)
                        .build(),
                ).build()

        CloudTasksClient.create(buildCloudTasksSettings()).use { client ->
            val createdTask = client.createTask(settings.recipientServiceQueryPath, task)
            logger.info("タスクの作成に成功しました。 name: ${createdTask.name}")
        }
    }
}
