package community.flock.pragmatic.domain.user

import arrow.core.left
import arrow.core.right
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.UserMother
import java.util.UUID

class TestUserRepository : UserRepository {
    private val users = mutableMapOf(UserMother.getUserWithId().let { it.id.value to it })

    override fun getAll() = users.values.toList().right()

    override fun getById(userId: User.Id.Valid) = users[userId.value]?.right() ?: UserNotFound(userId).left()

    override fun save(user: User<User.Id.Absent>) =
        User(
            id = User.Id.Valid(UUID.randomUUID()),
            firstName = user.firstName,
            lastName = user.lastName,
            birthDay = user.birthDay,
        ).also { users[it.id.value] = it }.right()

    override fun deleteById(userId: User.Id.Valid) = users.remove(userId.value)?.right() ?: UserNotFound(userId).left()
}
