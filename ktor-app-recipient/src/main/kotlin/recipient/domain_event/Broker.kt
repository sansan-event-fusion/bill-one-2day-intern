package recipient.domain_event

import recipient.domain.invoice.RecipientInvoicePublished
import recipient.domain_event.subscriber.RecipientInvoiceReflect

class Broker {
    companion object {
        val pubsub =
            mapOf(
                RecipientInvoicePublished::class.qualifiedName to listOf(RecipientInvoiceReflect()),
            )
    }
}
