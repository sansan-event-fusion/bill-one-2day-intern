package issuing.fixture

import com.ninja_squad.dbsetup.operation.Operation
import issuing.testing.AssociatingContext
import issuing.testing.buildLoadingFixturesOperation
import java.util.*

class IssuerTableFixture(
  val tenantNameId: String = "yonyon",
  val issuerUuid: UUID = UUID.fromString("0eb3bd13-1d88-4fc8-bde3-115884648a91"),
  val issuerName: String = "issuerName",
  val issuerEmail: String = "issuerEmail",
)

fun buildIssuerOperation(fixtureAccounts: List<IssuerTableFixture>): Operation {
  return buildLoadingFixturesOperation(fixtureAccounts, "issuer")
}

fun AssociatingContext<TenantTableFixture>.insertIssuer(
  issuerName: String,
  issuerEmail: String,
  callback: IssuerTableFixture.() -> IssuerTableFixture = { this },
) = insert(
  IssuerTableFixture(
    tenantNameId = parent.tenantNameId,
    issuerUuid = UUID.randomUUID(),
    issuerName = issuerName,
    issuerEmail = issuerEmail,
  ).callback(),
)
