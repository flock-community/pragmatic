package community.flock.pragmatic.domain.user.model

import java.util.UUID

object UserMother {

    val userId = User.Id.Valid(UUID.fromString("6da28373-7bf7-4efd-a3f5-0e78dd80b209"))

    fun createUser() = User(
        id = User.Id.Valid(UUID.randomUUID()),
        firstName = FirstName("FirstName").getOrNull()!!,
        lastName = LastName("LastName").getOrNull()!!,
    )

    fun getUserWithId(userId: User.Id.Valid = this.userId) = createUser().copy(id = userId)
}
