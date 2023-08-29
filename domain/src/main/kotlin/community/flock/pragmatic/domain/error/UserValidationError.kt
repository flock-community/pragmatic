package community.flock.pragmatic.domain.error

sealed class UserValidationError(message:String) : ValidationError(message)

sealed class FirstNameValidationError(message:String) : UserValidationError(message) {
    object Empty : FirstNameValidationError("First name cannot be empty")
}

sealed class LastNameValidationError(message:String) : UserValidationError(message) {
    object Empty : LastNameValidationError("Last name cannot be empty")
}
