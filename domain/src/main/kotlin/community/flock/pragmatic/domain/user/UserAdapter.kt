package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

interface UserAdapter {
    suspend fun getAll(): List<User<Id.Valid>>

    suspend fun getById(id: Int): User<Id.Valid>?

    suspend fun save(user: User<Id.NonExisting>): User<Id.Valid>

    suspend fun deleteById(id: Int): User<Id.Valid>?
}

interface HasUserAdapter {
    val userAdapter: UserAdapter
}
