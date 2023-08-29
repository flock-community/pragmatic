package community.flock.pragmatic.domain.user.model

import arrow.core.raise.either
import arrow.core.raise.ensure
import community.flock.pragmatic.domain.data.Value

data class User<T : User.Id>(
    val id: T,
    val firstName: FirstName.Valid,
    val lastName: LastName.Valid,
) {
    sealed interface Id {
        @JvmInline
        value class Valid(override val value: Int) : Value<Int>, Id
        object NonExisting : Id
    }

    companion object {
        operator fun invoke(firstName: FirstName.Valid, lastName: LastName.Valid) = User(
            id = Id.NonExisting,
            firstName = firstName,
            lastName = lastName
        )
    }
}

sealed interface UserValidationError

sealed interface FirstName {
    sealed interface NotValid : FirstName, UserValidationError {
        object Empty : NotValid
    }

    @JvmInline
    value class Valid private constructor(override val value: String) : FirstName, Value<String> {
        companion object {
            operator fun invoke(s: String) = either {
                ensure(s.isNotBlank()) { NotValid.Empty }
                Valid(s)
            }
        }
    }

    companion object {
        operator fun invoke(s: String) = Valid(s)
    }
}

sealed interface LastName {

    @JvmInline
    value class Valid private constructor(override val value: String) : LastName, Value<String> {
        companion object {
            operator fun invoke(s: String) = either {
                ensure(s.isNotBlank()) { NotValid.Empty }
                Valid(s)
            }
        }
    }

    sealed interface NotValid : LastName, UserValidationError {
        object Empty : NotValid
    }

    companion object {
        operator fun invoke(s: String) = Valid(s)
    }
}
