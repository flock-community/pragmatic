package community.flock.pragmatic.app.exceptions

sealed class AppException(override val message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class TechnicalException(cause: Throwable, override val message: String) :
    AppException(message, cause) {
    companion object {
        operator fun invoke(cause: Throwable, message: String? = null) =
            TechnicalException(cause, message ?: "Technical Exception, check cause")
    }
}

sealed class BusinessException(message: String) : AppException(message) {
    override fun fillInStackTrace() = this
}
