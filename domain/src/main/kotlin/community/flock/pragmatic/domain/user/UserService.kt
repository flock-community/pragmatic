package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

interface UserContext : HasUserRepository

object UserService {
    suspend fun UserContext.getUsers() = userRepository.getAll()

    suspend fun UserContext.getUserById(id: Id.Valid) = userRepository.getById(id)

    suspend fun UserContext.saveUser(user: User<Id.NonExisting>) = userRepository.save(user)

    suspend fun UserContext.deleteUserById(id: Id.Valid) = userRepository.deleteById(id)
}
