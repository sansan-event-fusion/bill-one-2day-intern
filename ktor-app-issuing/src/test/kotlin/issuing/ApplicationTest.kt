package issuing

import io.ktor.http.*
import io.ktor.server.testing.*
import issuing.testing.Database
import issuing.testing.testSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

class ApplicationTest {
  companion object {
    private val schemaName = this::class.java.declaringClass.simpleName.lowercase()
    private val settings = testSettings(schemaName)

    private val database = Database(settings, schemaName)

    @AfterClass
    @JvmStatic
    fun tearDownClass() {
      database.tearDown()
    }
  }

  @Before
  fun before() {
    database.setUpTable(emptyList())
  }

  @Test
  fun testIndex() =
    testApp {
      handleRequest(HttpMethod.Get, "/") {
      }.apply {
        assertThat(response.status()?.value).isEqualTo(200)
        assertThat(response.content!!).isEqualTo("bill-one-1day-intern kotlin-app-issuing is up!")
      }
    }

  private fun testApp(callback: TestApplicationEngine.() -> Unit) {
    withTestApplication({ module(true, settings) }) { callback() }
  }
}
