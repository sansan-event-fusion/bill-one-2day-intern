package issuing.testing

import issuing.settingsFromEnv

fun testSettings(schemaName: String) =
  settingsFromEnv().copy(
    jdbcUrl = testJdbcUrl(),
    environment = "test",
    schema = schemaName,
  )

private fun testJdbcUrl() =
  System.getenv("JDBC_URL")
    ?: "jdbc:postgresql://localhost:5432/bill-one-2024-summer-issuing-test?user=postgres"
