package issuing.fixture

import issuing.testing.AssociatingContext
import issuing.testing.buildLoadingFixturesOperation
import com.ninja_squad.dbsetup.operation.Operation
import java.time.OffsetDateTime
import java.util.*

class IssuerInvoiceTableFixture(
  val tenantNameId: String = "yonyon",
  val issuerInvoiceUuid: UUID = UUID.fromString("a5702dc4-6c21-41de-8035-d240f87f688f"),
  val supplierTenantNameId: String = "gogo",
  val recipientUuid: UUID = UUID.fromString("c2dbf383-795f-40cd-b694-f9d372ec27b7"),
  val issuerUuid: UUID = UUID.fromString("0eb3bd13-1d88-4fc8-bde3-115884648a91"),
  val invoiceAmount: Int = 1000,
  val paymentDeadline: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
  val uploadedFileStoragePath: String? = null,
)

fun buildIssuerInvoiceOperation(fixtureAccounts: List<IssuerInvoiceTableFixture>): Operation {
  return buildLoadingFixturesOperation(fixtureAccounts, "issuer_invoice")
}

fun AssociatingContext<IssuerTableFixture>.insertIssuerInvoice(
  recipientTenantNameId: String,
  issuerInvoiceUuid: UUID,
  invoiceAmount: Int,
  paymentDeadline: OffsetDateTime,
  uploadedFileStoragePath: String?,
  callback: IssuerInvoiceTableFixture.() -> IssuerInvoiceTableFixture = { this },
): IssuerInvoiceTableFixture =
  insert(
    IssuerInvoiceTableFixture(
      tenantNameId = recipientTenantNameId,
      issuerInvoiceUuid = issuerInvoiceUuid,
      supplierTenantNameId = parent.tenantNameId,
      issuerUuid = parent.issuerUuid,
      invoiceAmount = invoiceAmount,
      paymentDeadline = paymentDeadline,
      uploadedFileStoragePath = uploadedFileStoragePath,
    ).callback(),
  )
