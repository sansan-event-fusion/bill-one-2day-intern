package issuing.domain.issuer_invoice

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import issuing.domain.issuer.IssuerUUID
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId
import java.time.OffsetDateTime
import java.util.*

data class IssuerInvoice(
    val tenantNameId: TenantNameId,
    val supplierTenantNameId: TenantNameId,
    val issuerInvoiceUUID: IssuerInvoiceUUID,
    val issuerUUID: IssuerUUID,
    val invoiceAmount: InvoiceAmount,
    val paymentDeadline: PaymentDeadline,
    val recipientUUID: RecipientUUID, // 取引先
    val uploadedFileStoragePath: UploadedFileStoragePath? = null,
) {
    companion object {
        fun of(
            tenantNameId: TenantNameId,
            supplierTenantNameId: TenantNameId,
            issuerUUID: IssuerUUID,
            invoiceAmount: InvoiceAmount,
            paymentDeadline: PaymentDeadline,
            recipientUUID: RecipientUUID,
            issuerInvoiceUUID: IssuerInvoiceUUID,
            uploadedFileStoragePath: UploadedFileStoragePath? = null,
        ) = IssuerInvoice(
            tenantNameId = tenantNameId,
            supplierTenantNameId = supplierTenantNameId,
            issuerInvoiceUUID = issuerInvoiceUUID,
            issuerUUID = issuerUUID,
            invoiceAmount = invoiceAmount,
            paymentDeadline = paymentDeadline,
            recipientUUID = recipientUUID,
            uploadedFileStoragePath = uploadedFileStoragePath,
        )

        fun create(
            tenantNameId: TenantNameId,
            supplierTenantNameId: TenantNameId,
            issuerUUID: IssuerUUID,
            invoiceAmount: InvoiceAmount,
            paymentDeadline: PaymentDeadline,
            recipientUUID: RecipientUUID,
        ) = IssuerInvoice(
            issuerInvoiceUUID = IssuerInvoiceUUID.of(),
            supplierTenantNameId = supplierTenantNameId,
            issuerUUID = issuerUUID,
            invoiceAmount = invoiceAmount,
            paymentDeadline = paymentDeadline,
            recipientUUID = recipientUUID,
            tenantNameId = tenantNameId,
            uploadedFileStoragePath = null,
        )
    }

    fun reflectIssueResult(uploadedFileStoragePath: UploadedFileStoragePath) = this.copy(uploadedFileStoragePath = uploadedFileStoragePath)
}

data class IssuerInvoiceUUID
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    constructor(
        @JsonValue val value: UUID,
    ) {
        companion object {
            fun of() = IssuerInvoiceUUID(UUID.randomUUID())
        }
    }

data class InvoiceAmount
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    constructor(
        @JsonValue val value: Int,
    ) {
        init {
            require(value >= 0) { "invoiceAmountは0以上である必要があります。" }
        }
    }

data class PaymentDeadline
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    constructor(
        @JsonValue val value: OffsetDateTime,
    )

data class UploadedFileStoragePath
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    constructor(
        @JsonValue val value: String?,
    )
