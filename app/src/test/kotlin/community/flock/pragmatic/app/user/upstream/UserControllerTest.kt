package community.flock.pragmatic.app.user.upstream

import arrow.core.right
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class UserControllerTest {

    private val testRepository = object : UserRepository {

        override suspend fun getAll() = User(
            User.Id.Valid(UUID.randomUUID()),
            FirstName("FirstName").getOrNull()!!,
            LastName("LastName").getOrNull()!!,
            BirthDay("2020-01-01").getOrNull()!!,
        ).let(::flowOf).right()

        override suspend fun getById(userId: User.Id.Valid) = TODO("Not yet implemented")

        override suspend fun save(user: User<User.Id.NonExisting>) = TODO("Not yet implemented")

        override suspend fun deleteById(userId: User.Id.Valid) = TODO("Not yet implemented")
    }

    private val testLayer = object : UserControllerDependencies {
        override val userRepository = testRepository
    }

    @Test
    fun `UserController function getUsers should return a list of UserDto`() = runBlocking {
        UserController(testLayer)
            .getUsers()
            .first()
            .run {
                assertEquals("FirstName", firstName)
                assertEquals("LastName", lastName)
            }
    }
}
