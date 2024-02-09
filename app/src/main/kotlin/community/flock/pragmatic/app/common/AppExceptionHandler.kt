package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.exceptions.AppException
import community.flock.pragmatic.app.exceptions.BusinessException
import community.flock.pragmatic.app.exceptions.DomainException
import community.flock.pragmatic.app.exceptions.TechnicalException
import community.flock.pragmatic.app.exceptions.ValidationException
import community.flock.pragmatic.domain.error.DomainError
import community.flock.pragmatic.domain.error.UserNotFound
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.internalServerError
import org.springframework.http.ResponseEntity.notFound
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AppExceptionHandler {
    @ExceptionHandler(AppException::class)
    fun handleException(exception: AppException) =
        exception.run {
            when (this) {
                is BusinessException -> handle()
                is TechnicalException -> internalServerError().body(message)
            }
        }
}

private fun BusinessException.handle() =
    when (this) {
        is DomainException -> error.handle()
        is ValidationException -> badRequest().body(message)
    }

private fun DomainError.handle() =
    when (this) {
        is UserNotFound -> notFound().build<String>()
    }
