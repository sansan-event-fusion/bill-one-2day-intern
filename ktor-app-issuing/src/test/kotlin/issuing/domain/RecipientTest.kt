package issuing.domain

import issuing.domain.recipient.RecipientEmail
import issuing.domain.recipient.RecipientOrganizationName
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class RecipientTest {
    @Test
    fun `RecipientOraganizationName should not be blank`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                RecipientOrganizationName("")
            }
        assertEquals("recipientOrganizationNameは空文字にできません", exception.message)
    }

    @Test
    fun `RecipientEmail should not be blank`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                RecipientEmail("")
            }
        assertEquals("recipientEmailは空文字にできません", exception.message)
    }
}
