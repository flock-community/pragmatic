package community.flock.pragmatic.domain.error

sealed interface Error {
    val message: String
}

class TechnicalError(
    override val cause: Throwable,
    override val message: String,
) : RuntimeException(message, cause),
    Error {
    companion object {
        operator fun invoke(
            cause: Throwable,
            message: String? = null,
        ) = TechnicalError(cause, message ?: "Technical Exception, check cause")
    }
}

sealed class DomainError(
    override val message: String,
) : Error

sealed class ValidationError(
    override val message: String,
) : Error

sealed class SingleValidationError(
    message: String,
) : ValidationError(message)

data object UUIDError : SingleValidationError("Not a valid UUID")

class MultipleValidationErrors(
    val errors: Set<SingleValidationError>,
) : ValidationError("Validation failed: ${errors.joinToString { it.message }}")
