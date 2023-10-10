package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.UserMother
import java.util.UUID

class TestUserAdapter : UserAdapter {

    private val users = mutableMapOf(UserMother.getUserWithId().let { it.id() to it })

    override suspend fun getAll(): List<User<User.Id.Valid>> = users.values.toList()

    override suspend fun getById(userId: User.Id.Valid): User<User.Id.Valid>? = users[userId()]

    override suspend fun save(user: User<User.Id.NonExisting>): User<User.Id.Valid> = User(
        id = User.Id.Valid(UUID.randomUUID()),
        firstName = user.firstName,
        lastName = user.lastName,
    ).also { users[it.id()] = it }

    override suspend fun deleteById(userId: User.Id.Valid): User<User.Id.Valid>? = users.remove(userId())
}
