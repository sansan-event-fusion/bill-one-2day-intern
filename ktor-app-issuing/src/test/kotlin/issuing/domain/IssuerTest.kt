package issuing.domain

import issuing.domain.issuer.IssuerName
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class IssuerTest {
  @Test
  fun `IssuerNameの長さが1文字以上50文字以下であること`() {
    val exception =
      assertThrows<IllegalArgumentException> {
        IssuerName("")
      }
    assertEquals("issuerNameは空文字にできません。", exception.message)

  }

  @Test
  fun `issuerNameの長さが50文字以下であること`() {

    val exception =
      assertThrows<IllegalArgumentException> {
        IssuerName("A".repeat(51))
      }
    assertEquals("issuerNameの長さは1文字以上、255文字以下である必要があります。", exception.message)
  }
}
