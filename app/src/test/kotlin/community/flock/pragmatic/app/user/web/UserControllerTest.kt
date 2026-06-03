package community.flock.pragmatic.app.user.web

import arrow.core.right
import community.flock.pragmatic.api.wirespec.endpoint.GetUsers
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.UserService
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import io.kotest.assertions.fail
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.UUID

class UserControllerTest {
    private val testRepository =
        object : UserRepository {
            override fun getAll() =
                User(
                    User.Id.Valid(UUID.randomUUID()),
                    FirstName("FirstName").getOrNull()!!,
                    LastName("LastName").getOrNull()!!,
                    BirthDay("2020-01-01").getOrNull()!!,
                ).let(::listOf).right()

            override fun getById(userId: User.Id.Valid) = TODO("Not yet implemented")

            override fun save(user: User<User.Id.Absent>) = TODO("Not yet implemented")

            override fun deleteById(userId: User.Id.Valid) = TODO("Not yet implemented")
        }

    private val testLayer =
        object : UserControllerDependencies {
            override val userService =
                object : UserService {
                    override val userRepository = testRepository
                }
        }

    @Test
    fun `UserController function getUsers should return a list of UserDto`(): Unit =
        runBlocking {
            when (val response = UserController(testLayer).getUsers(GetUsers.Request)) {
                is GetUsers.Response200 -> response.body
                is GetUsers.Response500 -> fail("Unexpected response: ${response.body}")
            }.first().shouldNotBeNull {
                firstName shouldBe "FirstName"
                lastName shouldBe "LastName"
            }
        }
}
