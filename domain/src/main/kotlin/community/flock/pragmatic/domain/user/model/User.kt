package community.flock.pragmatic.domain.user.model

data class User<T : User.Id>(
    val id: T,
    val firstName: String,
    val lastName: String
) {
    sealed interface Id {
        @JvmInline
        value class Valid(val value: Int) : Id
        object NonExisting : Id
    }

    companion object {
        operator fun invoke(firstName: String, lastName: String) = User(
            id = Id.NonExisting,
            firstName = firstName,
            lastName = lastName
        )
    }
}
