package community.flock.pragmatic.app.user.upstream

import arrow.core.EitherNel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.app.common.Validator
import community.flock.pragmatic.domain.error.UserValidationError
import community.flock.pragmatic.domain.error.ValidationError
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id
import community.flock.wirespec.generated.WsPotentialUserDto

object UserConsumer : Validator<ValidationError, PotentialUserDto, User<Id.NonExisting>> {
    override fun PotentialUserDto.consume(): EitherNel<UserValidationError, User<Id.NonExisting>> = either {
        zipOrAccumulate(
            { FirstName(firstName).bind() },
            { LastName(lastName).bind() },
            { BirthDay(birthDay).bind() },
            User.Companion::invoke
        )
    }
}

object WsUserConsumer : Validator<ValidationError, WsPotentialUserDto, User<Id.NonExisting>> {
    override fun WsPotentialUserDto.consume(): EitherNel<UserValidationError, User<Id.NonExisting>> = either {
        zipOrAccumulate(
            { FirstName(firstName).bind() },
            { LastName(lastName).bind() },
            { BirthDay(birthDate).bind() },
            User.Companion::invoke
        )
    }
}
