package community.flock.pragmatic.app.user.downstream

import arrow.core.left
import arrow.core.right
import community.flock.pragmatic.app.user.downstream.UserExternalizer.externalize
import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

class LiveUserRepository : UserRepository {

    private val user = User(
        firstName = FirstName("First Name").getOrNull()!!,
        lastName = LastName("Last Name").getOrNull()!!
    )

    private val userStore = mutableMapOf(
        user.externalize().let { it.id.value to it }
    )

    override suspend fun getAll() = userStore.values.toList().right()

    override suspend fun getById(userId: Id.Valid) =
        userStore[userId()]?.right() ?: UserNotFound(userId).left()

    override suspend fun save(user: User<Id.NonExisting>) = user.externalize()
        .also { userStore[it.id.value] = it }
        .right()

    override suspend fun deleteById(userId: Id.Valid) =
        userStore.remove(userId())?.right() ?: UserNotFound(userId).left()
}
