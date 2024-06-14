package issuing.domain.issuer

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import issuing.domain.tenant.TenantNameId
import java.util.*

data class Issuer(
  val issuerUUID: IssuerUUID,
  val issuerName: IssuerName,
  val issuerEmail: IssuerEmail,
  val tenantNameId: TenantNameId,
)

data class IssuerUUID
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue val value: UUID,
) {
  companion object {
    fun of() = IssuerUUID(UUID.randomUUID())
  }
}

data class IssuerName
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue val value: String,
) {
  companion object {
    const val MAX_LENGTH = 50
    const val MIN_LENGTH = 1

    fun of(value: String) = IssuerName(value)
  }

  init {
    require(value.isNotBlank()) { "issuerNameは空文字にできません。" }
    require(value.length in MIN_LENGTH..MAX_LENGTH) { "issuerNameの長さは1文字以上、255文字以下である必要があります。" }
  }
}

data class IssuerEmail
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue val value: String,
) {
  init {
    require(value.isNotBlank()) { "issuerEmailは空文字にできません。" }
  }
}
