package community.flock.pragmatic.app.user.database

import arrow.core.getOrElse
import community.flock.pragmatic.api.wirespec.model.PotentialUserDto
import community.flock.pragmatic.app.environment.WithCassandraContainer
import community.flock.pragmatic.app.environment.WithCassandraContainer.Companion.cassandra
import community.flock.pragmatic.app.user.web.UserConsumer.validate
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class LiveUserRepositoryTest : WithCassandraContainer {
    @Autowired
    private lateinit var userRepository: LiveUserRepository

    @Nested
    inner class Docker {
        @Test
        fun testInstance() {
            cassandra.isRunning shouldBe true
        }
    }

    @Nested
    inner class Repository {
        @Test
        fun testLiveUserRepository(): Unit =
            runBlocking {
                val user =
                    PotentialUserDto(
                        firstName = "FirstName",
                        lastName = "LastName",
                        birthDate = "2020-01-01",
                    ).validate().getOrElse { throw it }
                userRepository.save(user)
                userRepository.getAll().shouldBeRight().toList().run {
                    size shouldBe 1
                    first().run {
                        firstName.value shouldBe "FirstName"
                        lastName.value shouldBe "LastName"
                    }
                }
            }
    }
}
