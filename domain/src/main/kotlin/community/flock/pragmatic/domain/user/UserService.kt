package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

interface UserContext : HasUserAdapter

object UserService {

    suspend fun UserContext.getUsers() = userAdapter.getAll()

    suspend fun UserContext.getUserById(id: Int) = userAdapter.getById(id)

    suspend fun UserContext.saveUser(user: User<Id.NonExisting>) = userAdapter.save(user)

    suspend fun UserContext.deleteUserById(id:Int) = userAdapter.deleteById(id)

}
