package community.flock.pragmatic.app.user.web

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.either
import community.flock.pragmatic.api.user.UserApi
import community.flock.pragmatic.api.user.UserApi.Companion.BY_ID_PATH
import community.flock.pragmatic.api.user.UserApi.Companion.USERS_PATH
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.app.common.mappers.UUIDConsumer.validate
import community.flock.pragmatic.app.exceptions.AppException
import community.flock.pragmatic.app.exceptions.DomainException
import community.flock.pragmatic.app.exceptions.TechnicalException
import community.flock.pragmatic.app.exceptions.ValidationException
import community.flock.pragmatic.app.user.web.UserConsumer.validate
import community.flock.pragmatic.app.user.web.UserProducer.produce
import community.flock.pragmatic.app.user.web.UsersProducer.produce
import community.flock.pragmatic.domain.error.DomainError
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.error.TechnicalError
import community.flock.pragmatic.domain.error.ValidationError
import community.flock.pragmatic.domain.error.ValidationErrors
import community.flock.pragmatic.domain.user.HasUserRepository
import community.flock.pragmatic.domain.user.UserService
import community.flock.pragmatic.domain.user.deleteUserById
import community.flock.pragmatic.domain.user.getUserById
import community.flock.pragmatic.domain.user.getUsers
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.saveUser
import kotlinx.coroutines.flow.toList
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

interface UserControllerDependencies : HasUserRepository

@RestController
@RequestMapping(USERS_PATH)
class UserController(
    appLayer: UserControllerDependencies,
) : UserApi {
    private val userService =
        object : UserService {
            override val userRepository = appLayer.userRepository
        }

    @GetMapping
    override suspend fun getUsers() =
        either {
            val users = userService.getUsers().mapError().bind()
            users.toList().produce()
        }.handle()

    @GetMapping(BY_ID_PATH)
    override suspend fun getUserById(
        @PathVariable id: String,
    ) = either {
        val uuid = id.validate().bind()
        val user = userService.getUserById(User.Id.Valid(uuid)).mapError().bind()
        user.produce()
    }.handle()

    @PostMapping
    override suspend fun postUser(
        @RequestBody potentialUser: PotentialUserDto,
    ) = either {
        val user = potentialUser.validate().bind()
        val savedUser = userService.saveUser(user).mapError().bind()
        savedUser.produce()
    }.handle()

    @DeleteMapping(BY_ID_PATH)
    override suspend fun deleteUserById(
        @PathVariable("id") id: String,
    ) = either {
        val uuid = id.validate().bind()
        val user = userService.deleteUserById(User.Id.Valid(uuid)).mapError().bind()
        user.produce()
    }.handle()

    private fun <R> Either<Error, R>.mapError() =
        mapLeft {
            when (it) {
                is TechnicalError -> TechnicalException(cause = it.cause, message = it.message)
                is ValidationError -> ValidationException(errors = listOf(it))
                is ValidationErrors -> ValidationException(errors = it.errors)
                is DomainError -> DomainException(error = it)
            }
        }

    private fun <R> Either<AppException, R>.handle() = getOrElse { throw it }
}
