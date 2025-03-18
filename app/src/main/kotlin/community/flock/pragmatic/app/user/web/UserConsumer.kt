package community.flock.pragmatic.app.user.web

import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.api.wirespec.PotentialUserDto
import community.flock.pragmatic.app.common.Validator
import community.flock.pragmatic.domain.error.UserValidationError
import community.flock.pragmatic.domain.error.ValidationError
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

object UserConsumer : Validator<ValidationError, PotentialUserDto, User<Id.NonExisting>> {
    override fun PotentialUserDto.consume() =
        either<NonEmptyList<UserValidationError>, User<Id.NonExisting>> {
            zipOrAccumulate(
                { Id.NonExisting },
                { FirstName(firstName).bind() },
                { LastName(lastName).bind() },
                { BirthDay(birthDate).bind() },
                ::User,
            )
        }
}
