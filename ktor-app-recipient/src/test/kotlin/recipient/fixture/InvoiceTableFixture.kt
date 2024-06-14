package recipient.fixture

import com.ninja_squad.dbsetup.operation.Operation
import recipient.testing.AssociatingContext
import recipient.testing.buildLoadingFixturesOperation
import java.time.OffsetDateTime
import java.util.*


data class InvoiceTableFixture(
    val tenantNameId: String = "yonyon",
    val recipientUuid: UUID = UUID.randomUUID(),
    val invoiceUuid: UUID = UUID.randomUUID(),
    val registeredBy: String = "RECIPIENT_UPLOAD",
    val registeredAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00+09"),
    val paymentDeadline: OffsetDateTime = OffsetDateTime.parse("2050-01-01T00:00:00+09"),
    val invoiceAmount: Int = 0,
    val supplierUuid: UUID = UUID.randomUUID(),
    val supplierName: String = "test",
)

fun buildInvoiceOperation(fixtureAccounts: List<InvoiceTableFixture>): Operation {
    return buildLoadingFixturesOperation(fixtureAccounts, "invoice")
}

fun AssociatingContext<RecipientTableFixture>.insertInvoice(
    invoiceAmount: Int = 0,
    registeredAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00+09"),
    callback: InvoiceTableFixture.() -> InvoiceTableFixture = { this },
): InvoiceTableFixture = insert(
    InvoiceTableFixture(
        tenantNameId = parent.tenantNameId,
        recipientUuid = parent.recipientUuid,
        invoiceUuid = UUID.randomUUID(),
        invoiceAmount = invoiceAmount,
        registeredAt = registeredAt,
    ).callback()
)
