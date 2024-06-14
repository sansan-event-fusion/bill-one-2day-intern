package recipient.query_service

import recipient.domain.recipient.RecipientUUID
import recipient.domain.tenant.TenantNameId
import recipient.util.withHandle
import java.util.*

object RecipientQueryService {
    fun getRecipientOrNull(tenantNameId: TenantNameId, recipientUUID: RecipientUUID): RecipientRow? {
        return withHandle(tenantNameId) { handle ->
            handle.createQuery(RecipientRow.sql())
                .bind("tenantNameId", tenantNameId.value)
                .bind("recipientUUID", recipientUUID.value)
                .mapTo(RecipientRow::class.java)
                .singleOrNull()
        }
    }

    fun getRecipients(tenantNameId: TenantNameId): RecipientsQueryResult {
        return withHandle(tenantNameId) { handle ->
            val recipients = handle.createQuery(RecipientRow.allRecipientSql())
                .bind("tenantNameId", tenantNameId.value)
                .mapTo(RecipientRow::class.java)
                .set()

            RecipientsQueryResult.fromRow(recipients)
        }
    }
}


data class RecipientRow(
    val tenantNameId: String,
    val recipientUUID: UUID,
    val fullName: String,
    val email: String,
) {
    companion object {
        fun sql(): String {
            return """
                SELECT
                    tenant_name_id,
                    recipient_uuid,
                    full_name,
                    email
                FROM
                    recipient
                where
                    tenant_name_id = :tenantNameId
                    and recipient_uuid = :recipientUUID
            """.trimIndent()
        }

        fun allRecipientSql(): String {
            //language=postgresql
            return """
                SELECT
                    tenant_name_id,
                    recipient_uuid,
                    full_name,
                    email
                FROM
                    recipient
                where
                    tenant_name_id = :tenantNameId
                ORDER BY
                    created_at ASC
            """.trimIndent()
        }
    }

}

data class RecipientsQueryResult(
    val recipients: Set<RecipientRow>
) {
    companion object {
        fun fromRow(row: Set<RecipientRow>): RecipientsQueryResult {
            return RecipientsQueryResult(
                recipients = row
            )
        }
    }
}
