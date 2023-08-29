package community.flock.pragmatic.domain.user.model

import arrow.core.raise.either
import arrow.core.raise.ensure
import community.flock.pragmatic.domain.data.Value
import community.flock.pragmatic.domain.error.FirstNameValidationError
import community.flock.pragmatic.domain.error.LastNameValidationError

data class User<T : User.Id>(
    val id: T,
    val firstName: FirstName,
    val lastName: LastName,
) {
    sealed interface Id {
        @JvmInline
        value class Valid(override val value: Int) : Value<Int>, Id
        object NonExisting : Id
    }

    companion object {
        operator fun invoke(firstName: FirstName, lastName: LastName) = User(
            id = Id.NonExisting,
            firstName = firstName,
            lastName = lastName
        )
    }
}

@JvmInline
value class FirstName private constructor(override val value: String) : Value<String> {
    companion object {
        operator fun invoke(s: String) = either {
            ensure(s.isNotBlank()) { FirstNameValidationError.Empty }
            FirstName(s.trim())
        }
    }
}

@JvmInline
value class LastName private constructor(override val value: String) : Value<String> {
    companion object {
        operator fun invoke(s: String) = either {
            ensure(s.isNotBlank()) { LastNameValidationError.Empty }
            LastName(s.trim())
        }
    }
}
