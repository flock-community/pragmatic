package community.flock.pragmatic.app.user.upstream

import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.domain.common.Consumer
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

object UserConsumer : Consumer<PotentialUserDto, User<Id.NonExisting>> {
    override fun PotentialUserDto.consume() = User(
        firstName = firstName,
        lastName = lastName
    )
}
