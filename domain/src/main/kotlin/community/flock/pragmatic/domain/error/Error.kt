package community.flock.pragmatic.domain.error

sealed interface Error {
    val message: String
}

class TechnicalError(
    override val cause: Throwable,
    override val message: String,
) : Error, RuntimeException(message, cause) {
    companion object {
        operator fun invoke(cause: Throwable, message: String? = null) =
            TechnicalError(cause, message ?: "Technical Exception, check cause")
    }
}

sealed class DomainError(override val message: String) : Error

sealed class ValidationError(override val message: String) : Error

class ValidationErrors(val errors: List<ValidationError>) : Error {
    override val message = "Multiple Validation Errors: ${errors.joinToString { it.message }}"
}
