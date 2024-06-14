package recipient.domain

import org.junit.jupiter.api.Assertions.assertEquals
import recipient.domain.invoice.*
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import java.time.LocalDate
import java.util.*
import kotlin.test.Test

class InvoiceTest {
    companion object {
        val recipientUUID = RecipientUUID(UUID.randomUUID())
        val invoiceUUID = InvoiceUUID(UUID.randomUUID())
        val supplierUUID = SupplierUUID(UUID.randomUUID())
    }

    @Test
    fun `should create invoice`() {
        // given
        val tenantNameId = TenantNameId("yonyon")
        val recipientUUID = recipientUUID
        val registeredBy = RegisteredBy.RECEIVE
        val invoiceAmount = InvoiceAmount(100)
        val supplierUUID = supplierUUID
        val supplierName = SupplierName("supplierName")
        val paymentDeadline = PaymentDeadline(LocalDate.of(2021, 1, 1))

        val invoice = Invoice.of(
            tenantNameId,
            recipientUUID,
            registeredBy,
            invoiceAmount,
            supplierUUID,
            supplierName,
            paymentDeadline
        )

        // then
        assertEquals(tenantNameId, invoice.tenantNameId)
        assertEquals(recipientUUID, invoice.recipientUUID)
        assertEquals(registeredBy, invoice.registeredBy)
        assertEquals(invoiceAmount, invoice.invoiceAmount)
        assertEquals(supplierUUID, invoice.supplierUUID)
        assertEquals(supplierName, invoice.supplierName)
        assertEquals(paymentDeadline, invoice.paymentDeadline)
    }

    @Test
    fun `can update invoice`() {
        val invoice = Invoice(
            TenantNameId("yonyon"),
            invoiceUUID,
            recipientUUID,
            InvoiceAmount(100),
            supplierUUID,
            SupplierName("supplierName"),
            PaymentDeadline(LocalDate.of(2021, 1, 1)),
            RegisteredBy.RECEIVE,
            RegisteredAt.now()
        )
        val updatedInvoiceAmount = InvoiceAmount(99999)
        val updatedSupplierID = SupplierUUID(UUID.randomUUID())
        val updatedSupplierName = SupplierName("updatedSupplierName")
        val updatedPaymentDeadline = PaymentDeadline(LocalDate.of(1582, 6, 21))

        val updatedInvoice = invoice.update(
            updatedInvoiceAmount,
            updatedSupplierID,
            updatedSupplierName,
            updatedPaymentDeadline
        )

        assertEquals(invoice.invoiceUUID, updatedInvoice.invoiceUUID)
        assertEquals(updatedInvoiceAmount, updatedInvoice.invoiceAmount)
        assertEquals(updatedSupplierID, updatedInvoice.supplierUUID)
        assertEquals(updatedSupplierName, updatedInvoice.supplierName)
        assertEquals(updatedPaymentDeadline, updatedInvoice.paymentDeadline)
    }
}
