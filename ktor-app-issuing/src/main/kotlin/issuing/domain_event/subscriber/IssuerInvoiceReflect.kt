package issuing.domain_event.subscriber

import com.google.cloud.tasks.v2.*
import com.google.cloud.tasks.v2.HttpMethod
import com.google.protobuf.ByteString
import com.sansan.billone.lib.TraceContext
import io.ktor.http.*
import issuing.domain_event.Subscriber
import issuing.settings
import issuing.util.buildCloudTasksSettings
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

class IssuerInvoiceReflect : Subscriber {
  private val logger = LoggerFactory.getLogger(IssuerInvoiceReflect::class.java)

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
            .setUrl("${settings.issuingApiUrl}/event-handler/issuing/invoices/reflect-issued-result")
            .setOidcToken(oidcTokenBuilder)
            .setHttpMethod(HttpMethod.POST)
            .build(),
        ).build()

    CloudTasksClient.create(buildCloudTasksSettings()).use { client ->
      val createdTask = client.createTask(settings.issuingServiceQueryPath, task)
      logger.info("タスクの作成に成功しました。 name: ${createdTask.name}")
    }
  }
}
