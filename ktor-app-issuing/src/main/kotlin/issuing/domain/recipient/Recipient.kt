package issuing.domain.recipient

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import issuing.domain.tenant.TenantNameId
import java.util.*

data class Recipient(
  val tenantNameId: TenantNameId,
  val recipientUUID: RecipientUUID,
  val recipientName: RecipientName,
  val recipientEmail: RecipientEmail,
)

data class RecipientUUID
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue val value: UUID,
) {
  companion object {
    fun of() = RecipientUUID(UUID.randomUUID())
  }
}

data class RecipientName
@JsonCreator(
  mode = JsonCreator.Mode.DELEGATING,
)
constructor(
  @JsonValue val value: String,
) {
  init {
    require(value.isNotBlank()) { "recipientNameは空文字にできません" }
  }
}

data class RecipientOrganizationName
@JsonCreator(
  mode = JsonCreator.Mode.DELEGATING,
)
constructor(
  @JsonValue val value: String,
) {
  init {
    require(value.isNotBlank()) { "recipientOrganizationNameは空文字にできません" }
  }
}

data class RecipientEmail
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
  @JsonValue val value: String,
) {
  init {
    require(value.isNotBlank()) { "recipientEmailは空文字にできません" }
  }
}
