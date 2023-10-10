package community.flock.pragmatic.domain.user.model

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

class LastNameTest {

    @Test
    fun testLastName() {
        LastName("yolo").shouldBeRight()
    }

    @Test
    fun testEmptyLastName() {
        LastName("").shouldBeLeft()
    }
}
