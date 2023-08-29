package community.flock.pragmatic.app.common

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(Throwable::class)
    fun handleException(t: Throwable) = when (t) {
        is AppException -> t.handle()
        else -> ResponseEntity.internalServerError().body(t.message)
    }
}

private fun AppException.handle(): ResponseEntity<String> = when (this) {
    is AppException.TechnicalException -> handle()
    is AppException.BusinessException -> handle()
}

private fun AppException.TechnicalException.handle() = ResponseEntity.internalServerError().body(message)

private fun AppException.BusinessException.handle() = when (this) {
    is AppException.UserException -> handle()
    is AppException.ValidationException -> ResponseEntity.badRequest().body(message)
}

private fun AppException.UserException.handle() = when (this) {
    is AppException.UserException.UserNotFoundException -> ResponseEntity.status(NOT_FOUND).body(message)
}
