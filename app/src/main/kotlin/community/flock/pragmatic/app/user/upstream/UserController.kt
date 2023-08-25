package community.flock.pragmatic.app.user.upstream

import community.flock.pragmatic.api.common.apiBaseUrl
import community.flock.pragmatic.api.user.UserApi
import community.flock.pragmatic.app.user.downstream.LiveUserAdapter
import community.flock.pragmatic.app.user.upstream.UserConsumer.consume
import community.flock.pragmatic.app.user.upstream.UserProducer.produce
import community.flock.pragmatic.domain.user.HasUserAdapter
import community.flock.pragmatic.domain.user.UserContext
import community.flock.pragmatic.domain.user.UserService.deleteUserById
import community.flock.pragmatic.domain.user.UserService.getUserById
import community.flock.pragmatic.domain.user.UserService.getUsers
import community.flock.pragmatic.domain.user.UserService.saveUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import community.flock.pragmatic.api.user.request.PotentialUserDto as PotentialUser

private interface Ctx : UserContext

@RestController
@RequestMapping(apiBaseUrl)
class UserController<ENV>(appLayer: ENV) : UserApi where ENV : HasUserAdapter {

    private val ctx = object : Ctx {
        override val userAdapter = appLayer.userAdapter
    }

    @GetMapping(UserApi.path)
    override suspend fun getUsers() = ctx.getUsers().map { it.produce() }

    @GetMapping("${UserApi.path}/{id}")
    override suspend fun getUserById(@PathVariable id: String) = ctx.getUserById(id.toInt())?.produce()

    @PostMapping(UserApi.path)
    override suspend fun postUser(@RequestBody user: PotentialUser) = ctx.saveUser(user.consume()).produce()

    @DeleteMapping("${UserApi.path}/{id}")
    override suspend fun deleteUserById(@PathVariable("id") id: String) = ctx.deleteUserById(id.toInt())?.produce()

}
