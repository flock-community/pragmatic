package community.flock.pragmatic.domain.user

import arrow.core.left
import arrow.core.right
import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.UserMother
import kotlinx.coroutines.flow.asFlow
import java.util.UUID

class TestUserRepository : UserRepository {

    private val users = mutableMapOf(UserMother.getUserWithId().let { it.id() to it })

    override suspend fun getAll() = users.values.asFlow().right()

    override suspend fun getById(userId: User.Id.Valid) =
        users[userId()]?.right() ?: UserNotFound(userId).left()

    override suspend fun save(user: User<User.Id.NonExisting>) = User(
        id = User.Id.Valid(UUID.randomUUID()),
        firstName = user.firstName,
        lastName = user.lastName,
    ).also { users[it.id()] = it }.right()

    override suspend fun deleteById(userId: User.Id.Valid) =
        users.remove(userId())?.right() ?: UserNotFound(userId).left()
}
