package community.flock.pragmatic.app.user.web

import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.api.wirespec.model.PotentialUserDto
import community.flock.pragmatic.app.common.upstream.Validator
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

object UserConsumer : Validator<PotentialUserDto, User<Id.Absent>> {
    override fun PotentialUserDto.consume() =
        either {
            zipOrAccumulate(
                { Id.Absent },
                { FirstName(firstName).bind() },
                { LastName(lastName).bind() },
                { BirthDay(birthDate).bind() },
                ::User,
            )
        }
}
