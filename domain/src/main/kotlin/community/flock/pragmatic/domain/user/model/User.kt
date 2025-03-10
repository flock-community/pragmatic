package community.flock.pragmatic.domain.user.model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import community.flock.pragmatic.domain.data.Value
import community.flock.pragmatic.domain.error.BirthDayError
import community.flock.pragmatic.domain.error.FirstNameError
import community.flock.pragmatic.domain.error.LastNameError
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.util.UUID

data class User<T : User.Id>(
    val id: T,
    val firstName: FirstName,
    val lastName: LastName,
    val birthDay: BirthDay,
) {
    sealed interface Id {
        @JvmInline
        value class Valid(
            override val value: UUID,
        ) : Value<UUID>,
            Id

        data object NonExisting : Id
    }
}

@JvmInline
value class FirstName private constructor(
    override val value: String,
) : Value<String> {
    override fun toString() = value

    companion object {
        operator fun invoke(s: String) =
            either {
                ensure(s.isNotBlank()) { FirstNameError.Empty }
                FirstName(s.trim())
            }
    }
}

@JvmInline
value class LastName private constructor(
    override val value: String,
) : Value<String> {
    override fun toString() = value

    companion object {
        operator fun invoke(s: String) =
            either {
                ensure(s.isNotBlank()) { LastNameError.Empty }
                LastName(s.trim())
            }
    }
}

@JvmInline
value class BirthDay private constructor(
    override val value: LocalDate,
) : Value<LocalDate> {
    override fun toString() = value.toString()

    companion object {
        operator fun invoke(s: String) =
            Either
                .catch { LocalDate.parse(s, ISO_LOCAL_DATE) }
                .mapLeft { BirthDayError.Invalid }
                .map(::BirthDay)
    }
}
