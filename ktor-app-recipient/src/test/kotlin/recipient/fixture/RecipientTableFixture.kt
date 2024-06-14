package recipient.fixture

import com.ninja_squad.dbsetup.operation.Operation
import recipient.testing.AssociatingContext
import recipient.testing.buildLoadingFixturesOperation
import java.time.OffsetDateTime
import java.util.*

data class RecipientTableFixture(
    val tenantNameId: String = "yonyon",
    val recipientUuid: UUID = UUID.randomUUID(),
    val fullName: String = "受領者1",
    val email: String = "recipient@yonyon.com",
    val createdAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
)

fun buildRecipientOperation(fixtureAccounts: List<RecipientTableFixture>): Operation {
    return buildLoadingFixturesOperation(fixtureAccounts, "recipient")
}


fun AssociatingContext<TenantTableFixture>.insertRecipient(
    fullName: String? = null,
    email: String? = null,
    createdAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
    callback: RecipientTableFixture.() -> RecipientTableFixture = { this },
): RecipientTableFixture = insert(
    RecipientTableFixture(
        tenantNameId = parent.tenantNameId,
        fullName = fullName ?: "受領者1",
        email = email ?: "",
        createdAt = createdAt,
    ).callback()
)
