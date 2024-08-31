package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

interface UserService : HasUserRepository

suspend fun UserService.getUsers() = userRepository.getAll()

suspend fun UserService.getUserById(id: Id.Valid) = userRepository.getById(id)

suspend fun UserService.saveUser(user: User<Id.NonExisting>) = userRepository.save(user)

suspend fun UserService.deleteUserById(id: Id.Valid) = userRepository.deleteById(id)
