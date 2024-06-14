package recipient.infrastructure

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import recipient.domain.invoice.Invoice
import recipient.domain.invoice.InvoiceUUID
import recipient.domain.recipient.RecipientUUID
import recipient.settings
import recipient.util.createStorageObject
import recipient.util.createStorageUrl
import java.net.URL

object RecipientInvoiceStorage {
    fun upload(invoice: Invoice, pdf: ByteArray): BlobId {
        val blobId =
            BlobId.of(settings.recipientInvoiceBucket, attachmentPath(invoice.recipientUUID, invoice.invoiceUUID))
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build()

        createStorageObject(blobInfo, pdf)

        return blobId
    }

    fun getUrl(recipientUUID: RecipientUUID, invoiceUUID: InvoiceUUID): URL {
        val blobId = BlobId.of(settings.recipientInvoiceBucket, attachmentPath(recipientUUID, invoiceUUID))
        val blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build()
        return createStorageUrl(blobInfo)
    }

    private fun attachmentPath(recipientUUID: RecipientUUID, invoiceUUID: InvoiceUUID): String {
        return "recipient/${recipientUUID.value}/recipient-invoices/${invoiceUUID.value}.pdf"
    }

}
