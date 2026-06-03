package community.flock.pragmatic.app.user.database

import arrow.core.EitherNel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.app.common.downstream.Externalizer
import community.flock.pragmatic.app.common.downstream.Verifier
import community.flock.pragmatic.domain.error.SingleValidationError
import community.flock.pragmatic.domain.user.model.BirthDay
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

object UserConverter : Verifier<UserEntity, User<User.Id.Valid>>, Externalizer<User<User.Id.Absent>, UserEntity> {
    override fun UserEntity.internalize(): EitherNel<SingleValidationError, User<User.Id.Valid>> =
        either {
            zipOrAccumulate(
                { User.Id.Valid(id.value) },
                { FirstName(firstName).bind() },
                { LastName(lastName).bind() },
                { BirthDay(birthDay).bind() },
                ::User,
            )
        }

    override fun User<User.Id.Absent>.externalize(): UserEntity =
        UserEntity.new {
            firstName = this@externalize.firstName.value
            lastName = this@externalize.lastName.value
            birthDay = this@externalize.birthDay.value.format(ISO_LOCAL_DATE)
        }
}
