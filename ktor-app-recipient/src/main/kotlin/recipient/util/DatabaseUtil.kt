package recipient.util


import com.sansan.billone.lib.DomainEventContext
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.SqlLogger
import org.jdbi.v3.core.statement.StatementContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import recipient.domain.tenant.TenantNameId
import recipient.settings

val dataSource: HikariDataSource by lazy {
    val dataSource = HikariDataSource()
    dataSource.jdbcUrl = settings.jdbcUrl
    if (settings.environment == "test") {
        // テストでコネクションがパンクすることを防ぐため。
        dataSource.minimumIdle = 0
    }
    dataSource
}

val jdbi: Jdbi by lazy {
    Jdbi.create(dataSource)
        .installPlugins()
        .setSqlLogger(CustomSqlLogger())
}

class CustomSqlLogger : SqlLogger {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun logBeforeExecution(context: StatementContext) {
        logger.debug(context.rawSql)
    }
}

inline fun <T> withHandle(tenantNameId: TenantNameId, block: (handle: Handle) -> T): T {
    return jdbi.open().use { h ->
        setSchemaForTenant(h, tenantNameId)
        block(h)
    }
}

val runInTransactionLogger: Logger = LoggerFactory.getLogger("runInTransaction")

// ドメインイベントのタスク作成漏れを防ぐため、トランザクション内でドメインイベントを発行しない場合でも常にdomainEventContextを要求する
inline fun <T> runInTransaction(
    tenantNameId: TenantNameId,
    domainEventContext: DomainEventContext,
    crossinline block: (handle: Handle) -> T
): T {
    val result: T = jdbi.inTransaction<T, Exception> { h ->
        setSchemaForTenant(h, tenantNameId)
        block(h)
    }

    return result
}

private val configureSQL = """
        SELECT
            set_config('search_path', :schemaName, false),
            set_config('app.tenant_name_id', :tenantNameId, false)
""".trimIndent()

fun setSchemaForTenant(h: Handle, tenantNameId: TenantNameId) {
    h.createUpdate(configureSQL) // search_pathはリテラルで指定するので、bindしない。
        .bind("schemaName", settings.schema)
        .bind("tenantNameId", tenantNameId.value)
        .execute()
}
