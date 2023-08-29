package community.flock.pragmatic.app.common

import community.flock.pragmatic.domain.error.ValidationError

sealed class AppException(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause) {

    sealed class TechnicalException(cause: Throwable, override val message: String) : AppException(message, cause)

    sealed class BusinessException(message: String) : AppException(message) {
        override fun fillInStackTrace() = this
    }

    class ValidationException(errors: List<ValidationError>) : BusinessException(errors.joinToString { it.message })

    sealed class UserException(message: String) : BusinessException(message) {
        class UserNotFoundException(id: String) : UserException("User with id: $id not found")
    }

}
