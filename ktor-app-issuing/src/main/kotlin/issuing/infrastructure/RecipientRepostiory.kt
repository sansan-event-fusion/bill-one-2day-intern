package issuing.infrastructure

import issuing.domain.recipient.Recipient
import issuing.domain.recipient.RecipientEmail
import issuing.domain.recipient.RecipientName
import issuing.domain.recipient.RecipientUUID
import issuing.domain.tenant.TenantNameId
import issuing.util.withHandle
import org.jdbi.v3.core.Handle
import java.util.*

object RecipientRepository {
  fun register(
    recipient: Recipient,
    handle: Handle,
  ) {
    val sql =
      """
            INSERT INTO recipient (
                tenant_name_id,
                recipient_uuid,
                recipient_name,
                recipient_email,
                recipient_phone_number
            ) VALUES (
                :tenantNameId,
                :recipientUUID,
                :recipientName,
                :recipientEmail,
                :recipientPhoneNumber
            )
            """.trimIndent()
    handle.createUpdate(sql)
      .bind("tenantNameId", recipient.tenantNameId.value)
      .bind("recipientUUID", recipient.recipientUUID.value)
      .bind("recipientName", recipient.recipientName.value)
      .bind("email", recipient.recipientEmail.value)
      .execute()

  }

  fun getByTenantNameId(tenantNameId: TenantNameId): List<Recipient> {
    val sql =
      """
            SELECT
                tenant_name_id,
                recipient_uuid,
                full_name,
                email
            FROM recipient
            WHERE tenant_name_id = :tenantNameId
            """.trimIndent()

    withHandle(tenantNameId) { handle ->
      val recipients =
        handle.createQuery(sql)
          .bind("tenantNameId", tenantNameId.value)
          .mapTo(Recipient::class.java)
          .toList()
      return recipients
    }
  }

  fun getOrNUll(
    recipientUUID: RecipientUUID,
    handle: Handle,
  ): Recipient? {
    val sql =
      """
            SELECT
                tenant_name_id,
                recipient_uuid,
                full_name,
                email
            FROM recipient
            WHERE recipient_uuid = :recipientUUID
            """.trimIndent()

    return handle.createQuery(sql)
      .bind("recipientUUID", recipientUUID.value)
      .mapTo(RecipientRow::class.java)
      .singleOrNull()
      ?.toRecipient()
  }
}

data class RecipientRow(
  val tenant_name_id: String,
  val recipient_uuid: UUID,
  val full_name: String,
  val email: String,
) {
  fun toRecipient(): Recipient =
    Recipient(
      tenantNameId = TenantNameId(tenant_name_id),
      recipientUUID = RecipientUUID(recipient_uuid),
      recipientName = RecipientName(full_name),
      recipientEmail = RecipientEmail(email),
    )
}
