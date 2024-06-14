package issuing.fixture

import issuing.testing.AssociatingContext
import issuing.testing.buildLoadingFixturesOperation
import com.ninja_squad.dbsetup.operation.Operation
import java.util.*

data class RecipientTableFixture(
  val tenantNameId: String = "yonyon",
  val recipientUuid: UUID = UUID.randomUUID(),
  val fullName: String = "受領者1",
  val email: String = "recipient@yonyon.com",
)

fun buildRecipientOperation(fixtureAccounts: List<RecipientTableFixture>): Operation {
  return buildLoadingFixturesOperation(fixtureAccounts, "recipient")
}

fun AssociatingContext<TenantTableFixture>.insertRecipient(
  fullName: String = "受領者1",
  email: String = "recipient@test.com",
  callback: RecipientTableFixture.() -> RecipientTableFixture = { this },
): RecipientTableFixture =
  insert(
    RecipientTableFixture(
      tenantNameId = parent.tenantNameId,
      fullName = fullName,
      email = email,
    ).callback(),
  )
