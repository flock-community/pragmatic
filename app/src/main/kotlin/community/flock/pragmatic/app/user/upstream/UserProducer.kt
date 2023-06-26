package community.flock.pragmatic.app.user.upstream

import community.flock.pragmatic.api.user.response.UserDto
import community.flock.pragmatic.domain.common.Producer
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

object UserProducer : Producer<User<Id.Valid>, UserDto> {
    override fun User<Id.Valid>.produce() = UserDto(
        id = id.value.toString(),
        firstName = firstName,
        lastName = lastName,
    )
}
