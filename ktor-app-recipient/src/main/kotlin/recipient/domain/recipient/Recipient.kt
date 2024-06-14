package recipient.domain.recipient

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class Recipient(
    val recipientUUID: RecipientUUID,
    val fullName: RecipientFullName,
    val recipientEmail: RecipientEmail,
)


data class RecipientUUID @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: UUID) {

    companion object {
        fun of(value: UUID) = RecipientUUID(value)
    }
}

data class RecipientFullName @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@JsonValue val value: String) {
    init {
        require(value.isNotBlank()) { "fullNameは空文字にできません。" }
        require(value.length in 1..50) { "fullNameの長さは1文字以上、50文字以下である必要があります。" }
    }
}

data class RecipientEmail
@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
constructor(
    @JsonValue val value: String,
) {
    companion object {
        val INVALID = RecipientEmail("")
    }
}
