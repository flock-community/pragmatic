package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.user.UserService.getUserById
import community.flock.pragmatic.domain.user.UserService.getUsers
import community.flock.pragmatic.domain.user.model.UserMother
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

private interface TestContext : UserContext

class UserServiceTest {

    @Test
    fun testUserService() = inContext {
        getUsers().shouldBeRight().toList().apply {
            size shouldBe 1
        }.first().apply {
            firstName() shouldBe "FirstName"
            lastName() shouldBe "LastName"
        }
    }

    @Test
    fun `UserService should yield correct user by id`() = inContext {
        getUserById(UserMother.userId).shouldBeRight().apply {
            id shouldBe UserMother.userId
        }
    }

    private fun inContext(test: suspend TestContext.() -> Unit) = runBlocking {
        object : TestContext {
            override val userRepository = TestUserRepository()
        }.test()
    }
}
