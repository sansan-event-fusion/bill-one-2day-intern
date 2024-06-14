package recipient.fixture

import recipient.testing.RootFixtureContext
import java.time.OffsetDateTime
import java.util.*

data class TenantTableFixture(
    val tenantNameId: String = "yonyon",
    val tenantUuid: UUID,
    val locale: String = "JA",
    val createdAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
)


fun newTenantTableFixture(): TenantTableFixture {
    return TenantTableFixture(
        tenantNameId = "yonyon",
        tenantUuid = UUID.randomUUID(),
        locale = "JA",
    )
}

fun RootFixtureContext.insertTenant(
    tenantNameId: String,
    locale: String = "JA",
    createdAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
    callback: TenantTableFixture.() -> TenantTableFixture = { this },
): TenantTableFixture = insert(
    newTenantTableFixture().copy(tenantNameId = tenantNameId, locale = locale, createdAt = createdAt).callback()
)
