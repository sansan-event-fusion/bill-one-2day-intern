package issuing.domain_event

import com.google.cloud.tasks.v2.*
import com.google.cloud.tasks.v2.HttpMethod
import com.sansan.billone.lib.DomainEventContext
import io.ktor.http.*
import issuing.domain_event.subscriber.IssuerInvoiceReflect
import issuing.domain_event.subscriber.IssuerInvoiceRegistered
import issuing.settings
import issuing.util.buildCloudTasksSettings
import org.slf4j.LoggerFactory

class Broker {
  private val logger = LoggerFactory.getLogger(this::class.java)

  companion object {
    val pubsub =
      mapOf(
        IssuingInvoicePublished::class.qualifiedName to listOf(IssuerInvoiceReflect()),
        IssuerInvoicePublished::class.qualifiedName to listOf(IssuerInvoiceRegistered()),
      )
  }

  fun createTask(domainEventContext: DomainEventContext) {
    val oidcTokenBuilder = OidcToken.newBuilder().setServiceAccountEmail(settings.serviceAccount)


    val retryCount = 3

    val task =
      Task.newBuilder()
        .setHttpRequest(
          HttpRequest.newBuilder()
            .putHeaders("X-Cloud-Trace-Context", domainEventContext.traceContext.value)
            .putHeaders(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            .setUrl("${settings.issuingApiUrl}/domain-event-broker/${domainEventContext.callUUID.value}")
            .setOidcToken(oidcTokenBuilder)
            .setHttpMethod(HttpMethod.POST)
            .build(),
        ).build()

    CloudTasksClient.create(buildCloudTasksSettings()).use { client ->

      for (i in 1..retryCount) {
        try {
          val createdTask = client.createTask(settings.issuingServiceQueryPath, task)
          logger.info("タスクの作成に成功しました name: ${createdTask.name}")
          break
        } catch (e: Exception) {
          if (i == retryCount) {
            throw e
          }
        }
      }
    }
  }
}
