package community.flock.pragmatic.domain.user.model

import community.flock.pragmatic.domain.data.NotBlank
import community.flock.pragmatic.domain.data.Value

data class User<T : User.Id>(
    val id: T,
    val firstName: NotBlank,
    val lastName: NotBlank,
) {
    sealed interface Id {
        @JvmInline
        value class Valid(override val value: Int) : Value<Int>, Id
        object NonExisting : Id
    }

    companion object {
        operator fun invoke(firstName: NotBlank, lastName: NotBlank) = User(
            id = Id.NonExisting,
            firstName = firstName,
            lastName = lastName
        )
    }
}
