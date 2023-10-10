package community.flock.pragmatic.app.exceptions

import community.flock.pragmatic.domain.error.ValidationError

class InvalidUUID : BusinessException("Given id is not a valid UUID")

class ValidationException(errors: List<ValidationError>) : BusinessException(errors.joinToString { it.message })

sealed class UserException(message: String) : BusinessException(message)
class UserNotFoundException(id: String) : UserException("User with id: $id not found")
