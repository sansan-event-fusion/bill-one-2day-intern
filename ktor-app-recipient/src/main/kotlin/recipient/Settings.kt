package recipient

data class Settings(
    val jdbcUrl: String,
    val schema: String = "recipient",
    val environment: String = "dev",
    val invoiceAPIUrl: String = " http://localhost:8081",
    val recipientServiceQueryPath: String = "projects/bill-one-2024/locations/asia-northeast1/queues/recipient",
    val serviceAccount: String = "",
) {
    val recipientInvoiceBucket: String = "recipient-$environment-bucket"
    val senderInvoiceBucket: String = "sender-$environment-bucket"
    val cloudTasksEmulatorHost: String? = if (environment == "dev") "localhost:9090" else null
    val cloudStorageEmulatorHost: String? =
        if (environment == "dev" || environment == "test") "http://localhost:4443" else null
}

fun settingsFromEnv() =
    Settings(
        jdbcUrl = "jdbc:postgresql://localhost:5432/bill-one-2024-summer-recipient?user=postgres",
    )
