package community.flock.pragmatic.app.user.database

import community.flock.pragmatic.api.wirespec.model.PotentialUserDto
import community.flock.pragmatic.app.environment.WithPostgresContainer
import community.flock.pragmatic.domain.user.model.User
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import community.flock.pragmatic.app.user.web.UserConsumer.validate as validatePotentialUser

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class LiveUserRepositoryTest : WithPostgresContainer {
    @Autowired
    private lateinit var userRepository: LiveUserRepository

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
                    LastName = "LastName",
                    `birth-date` = "2020-01-01",
                ).validatePotentialUser().shouldBeRight()
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
