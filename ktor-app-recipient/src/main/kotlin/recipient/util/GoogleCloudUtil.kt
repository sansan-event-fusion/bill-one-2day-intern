package recipient.util

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ImpersonatedCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import recipient.settings
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

fun createStorageObject(blobInfo: BlobInfo, content: ByteArray) {
    val storage = buildServiceClient()
    storage.create(blobInfo, content)
}

fun createStorageUrl(targetBlobInfo: BlobInfo): URL {
    if (settings.cloudStorageEmulatorHost != null) {
        return URL("${settings.cloudStorageEmulatorHost}/${targetBlobInfo.bucket}/${targetBlobInfo.name}")
    }
    val storage = buildServiceClient()
    val signer = ImpersonatedCredentials.create(
        GoogleCredentials.getApplicationDefault(),
        settings.serviceAccount,
        listOf(),
        listOf(),
        60
    )
    return storage.signUrl(
        targetBlobInfo,
        60,
        TimeUnit.SECONDS,
        Storage.SignUrlOption.withV4Signature(),
        Storage.SignUrlOption.signWith(signer)
    )
}

@Throws(IOException::class)
fun getStorageObject(blobId: BlobId): ByteArray {
    val storage = buildServiceClient()

    return storage.readAllBytes(blobId)
}

private fun buildServiceClient(): Storage {
    return StorageOptions.newBuilder().apply {
        if (settings.cloudStorageEmulatorHost != null) {
            setHost(settings.cloudStorageEmulatorHost)
        }
    }.build()
        .service
}
