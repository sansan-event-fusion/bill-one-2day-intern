package recipient.domain.invoice

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Invoice(
    val tenantNameId: TenantNameId,
    val invoiceUUID: InvoiceUUID,
    val recipientUUID: RecipientUUID,
    val invoiceAmount: InvoiceAmount?,
    val supplierUUID: SupplierUUID?,
    val supplierName: SupplierName?,
    val paymentDeadline: PaymentDeadline?,
    val registeredBy: RegisteredBy,
    val registeredAt: RegisteredAt,
) {
    companion object {
        fun of(
            tenantNameId: TenantNameId,
            recipientUUID: RecipientUUID,
            registeredBy: RegisteredBy,
            invoiceAmount: InvoiceAmount?,
            supplierUUID: SupplierUUID?,
            supplierName: SupplierName?,
            paymentDeadline: PaymentDeadline?
        ) = Invoice(
            tenantNameId,
            InvoiceUUID.of(),
            recipientUUID,
            invoiceAmount,
            supplierUUID,
            supplierName,
            paymentDeadline,
            registeredBy,
            RegisteredAt.now()
        )

        fun from(
            tenantNameId: String,
            invoiceUUID: UUID,
            recipientUUID: UUID,
            invoiceAmount: Int?,
            supplierUUID: UUID?,
            supplierName: String?,
            paymentDeadline: String?,
            registeredBy: String,
            registeredAt: OffsetDateTime
        ) = Invoice(
            TenantNameId(tenantNameId),
            InvoiceUUID(invoiceUUID),
            RecipientUUID(recipientUUID),
            invoiceAmount?.let { InvoiceAmount(it) },
            supplierUUID?.let { SupplierUUID(it) },
            supplierName?.let { SupplierName(it) },
            paymentDeadline?.let { PaymentDeadline(LocalDate.parse(it)) },
            RegisteredBy.valueOf(registeredBy),
            RegisteredAt(registeredAt)
        )

        fun fromEvent(
            tenantNameId: TenantNameId,
            invoiceUUID: InvoiceUUID,
            recipientUUID: RecipientUUID,
            invoiceAmount: InvoiceAmount?,
            supplierUUID: SupplierUUID?,
            supplierName: SupplierName?,
            paymentDeadline: PaymentDeadline?,
            registeredBy: RegisteredBy,
            registeredAt: RegisteredAt
        ) = Invoice(
            tenantNameId,
            invoiceUUID,
            recipientUUID,
            invoiceAmount,
            supplierUUID,
            supplierName,
            paymentDeadline,
            registeredBy,
            registeredAt
        )
    }

    fun update(
        invoiceAmount: InvoiceAmount?,
        supplierUUID: SupplierUUID?,
        supplierName: SupplierName?,
        paymentDeadline: PaymentDeadline?
    ): Invoice {
        return this.copy(
            invoiceAmount = invoiceAmount,
            supplierUUID = supplierUUID,
            supplierName = supplierName,
            paymentDeadline = paymentDeadline
        )
    }
}

data class InvoiceUUID @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: UUID) {
    companion object {
        fun of() = InvoiceUUID(UUID.randomUUID())
    }
}

data class InvoiceAmount @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(val value: Int)

data class SupplierUUID @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: UUID) {
    companion object {
        fun of() = SupplierUUID(UUID.randomUUID())
    }
}

data class SupplierName @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: String) {
    init {
        require(value.isNotBlank()) { "tenantNameIdは空文字にできません。" }
        require(value.length in 3..20) { "tenantNameIdの長さは3文字以上、20文字以下である必要があります。" }
    }

    companion object {
        fun of(value: String) = SupplierName(value)
    }
}

data class PaymentDeadline @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: LocalDate) {
    companion object {
        fun of(value: LocalDate) = PaymentDeadline(value)
    }
}

data class RegisteredAt @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: OffsetDateTime) {
    companion object {
        fun now() = RegisteredAt(OffsetDateTime.now())
    }
}

enum class RegisteredBy {
    RECIPIENT_UPLOAD,
    RECEIVE,
}


private fun String.parseDatetime(): OffsetDateTime {
    val correctedString = if (this.endsWith("+09")) this.replace("+09", "+09:00") else this
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX")
    return OffsetDateTime.parse(correctedString, formatter)
}
