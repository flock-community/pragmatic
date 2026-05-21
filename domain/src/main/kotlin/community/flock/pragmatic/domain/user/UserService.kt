package community.flock.pragmatic.domain.user

import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

interface UserService : HasUserRepository

fun UserService.getUsers() = userRepository.getAll()

fun UserService.getUserById(id: Id.Valid) = userRepository.getById(id)

fun UserService.saveUser(user: User<Id.NonExisting>) = userRepository.save(user)

fun UserService.deleteUserById(id: Id.Valid) = userRepository.deleteById(id)
