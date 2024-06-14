package recipient.domain.tenant

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class Tenant(
    val tenantNameId: TenantNameId,
    val tenantUUID: TenantUUID,
    val tenantLocale: TenantLocale
)

data class TenantNameId @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: String) {
    companion object {
        private val tenantNameIdRegex = Regex("^[a-z0-9-]+$")
    }

    init {
        require(value.isNotBlank()) { "tenantNameIdは空文字にできません。" }
        require(value.length in 3..20) { "tenantNameIdの長さは3文字以上、20文字以下である必要があります。" }
        require(value.matches(tenantNameIdRegex)) { "tenantNameIdには英小文字と数字とハイフンのみ使用できます。" }
        require(!value.startsWith("-") && !value.endsWith("-") && !value.contains("--")) { "tenantNameIdのハイフンは、先頭や末尾には使えません。連続して使うこともできません。" }
    }

}


data class TenantUUID @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: UUID) {
    companion object {
        fun of() = TenantUUID(UUID.randomUUID())
    }
}

enum class TenantLocale {
    JA, EN;

    companion object {
        fun of(value: String): TenantLocale {
            println("value: $value")
            return when (value) {
                "JA" -> JA
                "EN" -> EN
                else -> throw IllegalArgumentException("Invalid locale")
            }
        }
    }
}
