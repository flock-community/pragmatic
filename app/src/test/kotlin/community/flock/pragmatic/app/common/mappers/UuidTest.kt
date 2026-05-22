package community.flock.pragmatic.app.common.mappers

import community.flock.pragmatic.app.user.web.UUIDTransformer.consume
import community.flock.pragmatic.app.user.web.UUIDTransformer.produce
import community.flock.pragmatic.domain.error.UUIDError
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.arrow.core.shouldContain
import io.kotest.matchers.shouldBe
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
            "wrong"
                .consume()
                .shouldBeLeft() shouldContain UUIDError
        }
    }
}
