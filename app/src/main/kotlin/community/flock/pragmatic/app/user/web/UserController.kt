package community.flock.pragmatic.app.user.web

import arrow.core.raise.Raise
import arrow.core.raise.either
import community.flock.pragmatic.api.wirespec.endpoint.DeleteUser
import community.flock.pragmatic.api.wirespec.endpoint.GetUserById
import community.flock.pragmatic.api.wirespec.endpoint.GetUsers
import community.flock.pragmatic.api.wirespec.endpoint.PostUser
import community.flock.pragmatic.api.wirespec.model.ErrorDto
import community.flock.pragmatic.app.user.web.UUIDTransformer.validate
import community.flock.pragmatic.app.user.web.UserConsumer.validate
import community.flock.pragmatic.app.user.web.UserProducer.produce
import community.flock.pragmatic.app.user.web.UsersProducer.produce
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.user.HasUserService
import community.flock.pragmatic.domain.user.deleteUserById
import community.flock.pragmatic.domain.user.getUserById
import community.flock.pragmatic.domain.user.getUsers
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.saveUser
import org.springframework.web.bind.annotation.RestController

interface UserControllerDependencies : HasUserService

interface UserApi :
    GetUsers.Handler,
    GetUserById.Handler,
    PostUser.Handler,
    DeleteUser.Handler

@RestController
class UserController(
    private val ctx: UserControllerDependencies,
) : UserApi {
    override suspend fun getUsers(request: GetUsers.Request): GetUsers.Response<*> =
        handle {
            ctx.userService
                .getUsers()
                .bind()
                .produce()
        }.fold(GetUsers::Response500, GetUsers::Response200)

    override suspend fun getUserById(request: GetUserById.Request): GetUserById.Response<*> =
        handle {
            val id =
                request.path.id.value
                    .validate()
                    .bind()
            ctx.userService
                .getUserById(User.Id.Valid(id))
                .bind()
                .produce()
        }.fold(GetUserById::Response500, GetUserById::Response200)

    override suspend fun postUser(request: PostUser.Request): PostUser.Response<*> =
        handle {
            val user = request.body.validate().bind()
            ctx.userService
                .saveUser(user)
                .bind()
                .produce()
        }.fold(PostUser::Response500, PostUser::Response200)

    override suspend fun deleteUser(request: DeleteUser.Request): DeleteUser.Response<*> =
        handle {
            val uuid =
                request.path.id.value
                    .validate()
                    .bind()
            ctx.userService
                .deleteUserById(User.Id.Valid(uuid))
                .bind()
                .produce()
        }.fold(DeleteUser::Response500, DeleteUser::Response200)
}

private fun <E : Error, A : Any> handle(block: Raise<E>.() -> A) = either { block() }.mapLeft { ErrorDto(it.message) }
