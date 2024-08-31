package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.UserMother
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

private interface TestContext {
    val userService: UserService
}

class UserServiceTest {
    @Test
    fun testUserService() =
        inContext {
            userService
                .getUsers()
                .shouldBeRight()
                .toList()
                .apply {
                    size shouldBe 1
                }.first()
                .apply {
                    firstName.value shouldBe "FirstName"
                    lastName.value shouldBe "LastName"
                }
        }

    @Test
    fun `UserService should yield correct user by id`() =
        inContext {
            userService
                .getUserById(UserMother.userId)
                .shouldBeRight()
                .id shouldBe UserMother.userId
        }
}

private fun inContext(test: suspend TestContext.() -> Unit) =
    runBlocking {
        object : TestContext {
            override val userService =
                object : UserService {
                    override val userRepository = TestUserRepository()
                }
        }.test()
    }
