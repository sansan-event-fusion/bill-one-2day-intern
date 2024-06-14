package recipient.fixture

import recipient.testing.AssociatingContext
import java.util.*

data class TenantUserTableFixture(
    val tenantNameId: String = "yonyon",
    val tenantUserUuid: UUID = UUID.randomUUID(),
)


fun AssociatingContext<TenantTableFixture>.insertTenantUser(
    callback: TenantUserTableFixture.() -> TenantUserTableFixture = { this },
): TenantUserTableFixture = insert(
    TenantUserTableFixture(
        tenantNameId = parent.tenantNameId,
        tenantUserUuid = UUID.randomUUID(),
    ).callback()
)
