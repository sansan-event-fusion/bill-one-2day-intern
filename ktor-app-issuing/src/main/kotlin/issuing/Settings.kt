package issuing

data class Settings(
  val jdbcUrl: String,
  val schema: String = "issuing",
  val environment: String = "dev",
  val recipientApiUrl: String = " http://localhost:8081",
  val issuingApiUrl: String = " http://localhost:8082",
  val functionApiUrl: String = " http://localhost:8400",
  val issuingServiceQueryPath: String = "projects/bill-one-2024/locations/asia-northeast1/queues/issuing",
  val functionServiceQueryPath: String = "projects/bill-one-2024/locations/asia-northeast1/queues/function",
  val serviceAccount: String = "",
) {
  val issuingInvoiceBucket: String = "issuing-$environment-bucket"
  val cloudTasksEmulatorHost: String? = if (environment == "dev") "localhost:9090" else null
  val cloudStorageEmulatorHost: String? =
    if (environment == "dev" || environment == "test") "http://localhost:4443" else null
}

fun settingsFromEnv() =
  Settings(
    jdbcUrl = "jdbc:postgresql://localhost:5432/bill-one-2024-summer-issuing?user=postgres",
  )
