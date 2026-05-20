package community.flock.pragmatic.app.user.database

import community.flock.pragmatic.api.wirespec.model.PotentialUserDto
import community.flock.pragmatic.app.environment.WithCassandraContainer
import community.flock.pragmatic.app.environment.WithCassandraContainer.Companion.cassandra
import community.flock.pragmatic.app.user.web.UserConsumer.validate
import community.flock.pragmatic.domain.user.model.User
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
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
        fun getTheDefaultUser(): Unit =
            runBlocking {
                val userId = User.Id("cf8c1fe6-fb9e-436f-883f-cf5ffba90629").shouldBeRight()
                userRepository.getById(userId).shouldBeRight().run {
                    firstName.value shouldBe "Default"
                    lastName.value shouldBe "User"
                }
            }

        @Test
        fun testLiveUserRepository(): Unit =
            runBlocking {
                val user =
                    PotentialUserDto(
                        firstName = "FirstName",
                        lastName = "LastName",
                        birthDate = "2020-01-01",
                    ).validate().shouldBeRight()
                userRepository.save(user)
                userRepository
                    .getAll()
                    .shouldBeRight()
                    .toList()
                    .shouldHaveSize(2)
                    .find { it.firstName.value == "FirstName" }
                    .shouldNotBeNull {
                        firstName.value shouldBe "FirstName"
                        lastName.value shouldBe "LastName"
                    }
            }
    }
}
