package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.exceptions.AppException
import community.flock.pragmatic.app.exceptions.BusinessException
import community.flock.pragmatic.app.exceptions.InvalidUUID
import community.flock.pragmatic.app.exceptions.TechnicalException
import community.flock.pragmatic.app.exceptions.UserException
import community.flock.pragmatic.app.exceptions.UserNotFoundException
import community.flock.pragmatic.app.exceptions.ValidationException
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
    is TechnicalException -> handle()
    is BusinessException -> handle()
}

private fun TechnicalException.handle() = ResponseEntity.internalServerError().body(message)

private fun BusinessException.handle() = when (this) {
    is UserException -> handle()
    is ValidationException -> ResponseEntity.badRequest().body(message)
    is InvalidUUID -> ResponseEntity.badRequest().body(message)
}

private fun UserException.handle() = when (this) {
    is UserNotFoundException -> ResponseEntity.status(NOT_FOUND).body(message)
}
