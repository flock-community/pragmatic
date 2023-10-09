package community.flock.pragmatic.app.user.upstream

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import community.flock.pragmatic.api.user.UserApi
import community.flock.pragmatic.api.user.UserApi.Companion.BY_ID_PATH
import community.flock.pragmatic.api.user.UserApi.Companion.USERS_PATH
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.app.common.AppException
import community.flock.pragmatic.app.common.AppException.UserException.UserNotFoundException
import community.flock.pragmatic.app.user.upstream.UserConsumer.consume
import community.flock.pragmatic.app.user.upstream.UserProducer.produce
import community.flock.pragmatic.domain.user.HasUserAdapter
import community.flock.pragmatic.domain.user.UserContext
import community.flock.pragmatic.domain.user.UserService.deleteUserById
import community.flock.pragmatic.domain.user.UserService.getUserById
import community.flock.pragmatic.domain.user.UserService.getUsers
import community.flock.pragmatic.domain.user.UserService.saveUser
import community.flock.pragmatic.domain.user.model.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

interface UserControllerDependencies : HasUserAdapter
private interface Context : UserContext

@RestController
@RequestMapping(USERS_PATH)
class UserController(appLayer: UserControllerDependencies) : UserApi {

    private val context = object : Context {
        override val userAdapter = appLayer.userAdapter
    }

    @GetMapping
    override suspend fun getUsers() = context.getUsers().map { it.produce() }

    @GetMapping(BY_ID_PATH)
    override suspend fun getUserById(@PathVariable id: String) = either {
        ensureNotNull(context.getUserById(id.toInt())) { UserNotFoundException(id) }
    }.handle()

    @PostMapping
    override suspend fun postUser(@RequestBody potentialUser: PotentialUserDto) = either {
        val user = potentialUser.consume().bind()
        context.saveUser(user)
    }.handle()

    @DeleteMapping(BY_ID_PATH)
    override suspend fun deleteUserById(@PathVariable("id") id: String) = either {
        ensureNotNull(context.deleteUserById(id.toInt())) { UserNotFoundException(id) }
    }.handle()

    private fun Either<AppException, User<User.Id.Valid>>.handle() = map { it.produce() }.getOrElse { throw it }


}
