package community.flock.pragmatic.domain.error

import community.flock.pragmatic.domain.user.model.User

sealed class UserDomainError(message: String) : DomainError(message)

class UserNotFound(id: User.Id.Valid) : UserDomainError("User with id: $id, not found")
