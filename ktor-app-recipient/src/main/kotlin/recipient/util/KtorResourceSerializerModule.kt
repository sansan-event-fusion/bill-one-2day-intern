package recipient.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

val KtorResourceSerializerModule = SerializersModule {
    contextual(
        LocalDate::class,
        object : KSerializer<LocalDate> {
            override val descriptor = PrimitiveSerialDescriptor(LocalDate::class.qualifiedName!!, PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): LocalDate {
                return LocalDate.parse(decoder.decodeString())
            }

            override fun serialize(encoder: Encoder, value: LocalDate) {
                encoder.encodeString(value.toString())
            }
        }
    )

    contextual(
        UUID::class,
        object : KSerializer<UUID> {
            override val descriptor = PrimitiveSerialDescriptor(UUID::class.qualifiedName!!, PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): UUID {
                return UUID.fromString(decoder.decodeString())
            }

            override fun serialize(encoder: Encoder, value: UUID) {
                encoder.encodeString(value.toString())
            }
        }
    )

    contextual(
        BigDecimal::class,
        object : KSerializer<BigDecimal> {
            override val descriptor = PrimitiveSerialDescriptor(BigDecimal::class.qualifiedName!!, PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): BigDecimal {
                return BigDecimal(decoder.decodeString())
            }

            override fun serialize(encoder: Encoder, value: BigDecimal) {
                encoder.encodeString(value.toPlainString())
            }
        }
    )

    contextual(
        OffsetDateTime::class,
        object : KSerializer<OffsetDateTime> {
            override val descriptor =
                PrimitiveSerialDescriptor(OffsetDateTime::class.qualifiedName!!, PrimitiveKind.STRING)

            override fun deserialize(decoder: Decoder): OffsetDateTime {
                return OffsetDateTime.parse(decoder.decodeString())
            }

            override fun serialize(encoder: Encoder, value: OffsetDateTime) {
                encoder.encodeString(value.toString())
            }
        }
    )
}
