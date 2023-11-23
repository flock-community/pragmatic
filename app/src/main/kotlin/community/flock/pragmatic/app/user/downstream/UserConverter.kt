package community.flock.pragmatic.app.user.downstream

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.domain.data.invoke
import community.flock.pragmatic.domain.error.ValidationErrors
import community.flock.pragmatic.domain.user.model.FirstName
import community.flock.pragmatic.domain.user.model.LastName
import community.flock.pragmatic.domain.user.model.User
import java.util.UUID

object UserInternalizer : Internalizer<UserEntity, Either<ValidationErrors, User<User.Id.Valid>>> {
    override fun UserEntity.internalize() = either {
        val user = zipOrAccumulate(
            { User.Id.Valid(userId) },
            { FirstName(firstName).bind() },
            { LastName(lastName).bind() },
            { id, first, last -> User(id, first, last) },
        )
        user
    }.mapLeft(::ValidationErrors)
}

object UserExternalizer : Externalizer<User<User.Id.NonExisting>, UserEntity> {
    override fun User<User.Id.NonExisting>.externalize() = UserEntity(
        userId = UUID.randomUUID(),
        firstName = firstName(),
        lastName = lastName(),
    )
}
