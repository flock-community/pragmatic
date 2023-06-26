package community.flock.pragmatic.app.user.downstream

import community.flock.pragmatic.app.user.downstream.UserExternalizer.externalize
import community.flock.pragmatic.domain.user.UserAdapter
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

class LiveUserAdapter : UserAdapter {

    private val userStore = mutableMapOf(
        User(firstName = "First", lastName = "Last").externalize().let { it.id.value to it }
    )

    override suspend fun getAll(): List<User<Id.Valid>> = userStore.toList().map { (_, user) -> user }

    override suspend fun getById(id: Int): User<Id.Valid>? = userStore[id]

    override suspend fun save(user: User<Id.NonExisting>): User<Id.Valid> = user.externalize()
        .also { userStore[it.id.value] = it }

    override suspend fun deleteById(id: Int): User<Id.Valid>? = userStore[id]
        ?.also { userStore.remove(id) }

}
