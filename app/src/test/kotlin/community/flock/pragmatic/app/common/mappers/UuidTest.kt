package community.flock.pragmatic.app.common.mappers

import community.flock.pragmatic.app.common.mappers.UUIDConsumer.consume
import community.flock.pragmatic.app.common.mappers.UUIDExternalizer.externalize
import community.flock.pragmatic.app.common.mappers.UUIDInternalizer.internalize
import community.flock.pragmatic.app.common.mappers.UUIDProducer.produce
import community.flock.pragmatic.app.exceptions.ParseException.UuidParseException
import community.flock.pragmatic.app.exceptions.TechnicalException
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class UuidTest {

    private val uuidString = "b48da74a-ef4a-4842-9f3e-2cc9153cdc89"
    private val uuid = UUID.fromString(uuidString)

    @Nested
    @DisplayName("With Producer: produce ")
    inner class Producer {
        @Test
        fun `correct uuid`() {
            uuid.produce() shouldBe uuidString
        }
    }

    @Nested
    @DisplayName("With Consumer: consume ")
    inner class Consumer {
        @Test
        fun `correct uuid string`() {
            uuidString.consume() shouldBeRight uuid
        }

        @Test
        fun `incorrect uuid string`() {
            "wrong".consume()
                .shouldBeLeft()
                .shouldBeInstanceOf<UuidParseException>()
        }
    }

    @Nested
    @DisplayName("With Externalizer: externalize ")
    inner class Externalizer {
        @Test
        fun `correct uuid`() {
            uuid.externalize() shouldBe uuidString
        }
    }

    @Nested
    @DisplayName("With Internalizer: internalize ")
    inner class Internalizer {
        @Test
        fun `correct uuid string`() {
            uuidString.internalize() shouldBeRight uuid
        }

        @Test
        fun `incorrect uuid string`() {
            "wrong".internalize()
                .shouldBeLeft()
                .shouldBeInstanceOf<TechnicalException>()
                .cause
                .shouldBeInstanceOf<UuidParseException>()
        }
    }
}
