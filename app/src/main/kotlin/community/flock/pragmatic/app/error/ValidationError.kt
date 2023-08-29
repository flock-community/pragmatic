package community.flock.pragmatic.app.error

sealed class ValidationError(val message: String)

class FirstNameEmpty : ValidationError("FirstName cannot be empty")
class LastNameEmpty : ValidationError("LastName cannot be empty")
