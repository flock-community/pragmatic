package community.flock.pragmatic.domain.common

sealed class AppException(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause) {

    sealed class TechnicalException(cause: Throwable, override val message: String) : AppException(message, cause)

    sealed class BusinessException(message: String) : AppException(message) {
        override fun fillInStackTrace() = this
    }

    sealed class UserException(message: String): BusinessException(message) {
        class UserNotFoundException : UserException("User not found")
    }

}
