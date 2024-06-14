package issuing.fixture

import issuing.testing.AssociatingContext
import java.util.*

class TenantUserTableFixture(
  val tenantNameId: String = "yonyon",
  val tenantUserUuid: UUID = UUID.randomUUID(),
)

fun AssociatingContext<TenantTableFixture>.insertTenantUser(callback: TenantUserTableFixture.() -> TenantUserTableFixture = { this }) =
  insert(
    TenantUserTableFixture(
      tenantNameId = parent.tenantNameId,
      tenantUserUuid = UUID.randomUUID(),
    ).callback(),
  )
