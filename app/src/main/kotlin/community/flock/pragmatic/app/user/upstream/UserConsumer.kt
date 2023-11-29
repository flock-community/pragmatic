package community.flock.pragmatic.app.user.upstream

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.app.common.Consumer
import community.flock.pragmatic.app.exceptions.ValidationException
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

typealias ValidatedUser = Either<ValidationException, User<Id.NonExisting>>

object UserConsumer : Consumer<PotentialUserDto, ValidatedUser> {
    override fun PotentialUserDto.consume(): ValidatedUser = either {
        zipOrAccumulate(
            { FirstName(firstName).bind() },
            { LastName(lastName).bind() },
            { BirthDay(birthDay).bind() },
            User.Companion::invoke
        )
    }.mapLeft(::ValidationException)
}
