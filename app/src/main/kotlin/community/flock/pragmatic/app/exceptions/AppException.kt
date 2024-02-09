package community.flock.pragmatic.app.exceptions

import community.flock.pragmatic.domain.error.DomainError
import community.flock.pragmatic.domain.error.ValidationError

sealed class AppException(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class TechnicalException(cause: Throwable, override val message: String) :
    AppException(message, cause) {
    companion object {
        operator fun invoke(
            cause: Throwable,
            message: String? = null,
        ) = TechnicalException(cause, message ?: "Technical Exception, check cause")
    }
}

sealed class BusinessException(message: String) : AppException(message) {
    override fun fillInStackTrace() = this
}

class DomainException(val error: DomainError) : BusinessException(error.message)

class ValidationException(errors: List<ValidationError>) : BusinessException(errors.joinToString { it.message })
