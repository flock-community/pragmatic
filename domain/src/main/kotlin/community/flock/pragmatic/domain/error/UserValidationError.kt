package community.flock.pragmatic.domain.error

sealed class UserValidationError(message: String) : ValidationError(message)

sealed class FirstNameError(message: String) : UserValidationError(message) {
    data object Empty : FirstNameError("First name cannot be empty")
}

sealed class LastNameError(message: String) : UserValidationError(message) {
    data object Empty : LastNameError("Last name cannot be empty")
}

sealed class BirthDayError(message: String) : UserValidationError(message) {
    data object Invalid : BirthDayError("Birthday is invalid")
}
