package issuing.fixture

import issuing.testing.RootFixtureContext
import java.time.OffsetDateTime
import java.util.*

data class TenantTableFixture(
  val tenantNameId: String = "yonyon",
  val tenantUuid: UUID,
  val createdAt: OffsetDateTime = OffsetDateTime.parse("2019-01-01T00:00:00Z"),
  val locale: String = "JA",
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
  callback: TenantTableFixture.() -> TenantTableFixture = { this },
) = insert(
  newTenantTableFixture().copy(tenantNameId = tenantNameId).callback(),
)
