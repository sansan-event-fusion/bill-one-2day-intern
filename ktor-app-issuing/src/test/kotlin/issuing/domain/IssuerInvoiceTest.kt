package issuing.domain

import issuing.domain.issuer.IssuerUUID
import issuing.domain.issuer_invoice.*
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class IssuerInvoiceTest {
  companion object {
    val tenantNameId = TenantNameId("yonyuon")
    val supplierTenantNameId = TenantNameId("gogo")
    val issuerInvoiceUUID = IssuerInvoiceUUID(UUID.randomUUID())
    val issuerUUID = IssuerUUID(UUID.randomUUID())
    val invoiceAmount = InvoiceAmount(100)
    val paymentDeadline = PaymentDeadline(OffsetDateTime.now())
    val recipientUUID = RecipientUUID(UUID.randomUUID())
  }

  @Test
  fun `can reflect storage path`() {
    val invoice = IssuerInvoice(
      tenantNameId,
      supplierTenantNameId,
      issuerInvoiceUUID,
      issuerUUID,
      invoiceAmount,
      paymentDeadline,
      recipientUUID
    )


    val filePath = UploadedFileStoragePath("path/to/file")
    val reflected = invoice.reflectIssueResult(filePath)

    assertEquals(invoice.issuerInvoiceUUID, reflected.issuerInvoiceUUID)
    assertEquals(filePath, reflected.uploadedFileStoragePath)
  }
}