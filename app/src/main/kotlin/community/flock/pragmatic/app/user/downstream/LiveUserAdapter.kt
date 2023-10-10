package community.flock.pragmatic.app.user.downstream

import arrow.core.getOrElse
import arrow.core.raise.either
import community.flock.pragmatic.app.user.downstream.UserExternalizer.externalize
import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.user.UserAdapter
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

class LiveUserAdapter : UserAdapter {

    private val firstName = FirstName("First Name")
    private val lastName = LastName("Last Name")
    private val user = either {
        User(firstName.bind(), lastName.bind())
    }.getOrElse { throw RuntimeException(it.toString()) }

    private val userStore = mutableMapOf(
        user.externalize().let { it.id.value to it }
    )

    override suspend fun getAll(): List<User<Id.Valid>> = userStore.toList().map { (_, user) -> user }

    override suspend fun getById(userId: Id.Valid): User<Id.Valid>? = userStore[userId()]

    override suspend fun save(user: User<Id.NonExisting>): User<Id.Valid> = user.externalize()
        .also { userStore[it.id.value] = it }

    override suspend fun deleteById(userId: Id.Valid): User<Id.Valid>? = userStore.remove(userId())
}
