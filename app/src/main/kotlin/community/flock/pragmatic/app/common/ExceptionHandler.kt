package community.flock.pragmatic.app.common

import community.flock.pragmatic.domain.common.AppException
import org.springframework.http.ResponseEntity

fun AppException.handle() = when (this) {
    is AppException.TechnicalException -> handle()
    is AppException.BusinessException -> handle()
}

fun AppException.TechnicalException.handle() = ResponseEntity.internalServerError().body(message)

fun AppException.BusinessException.handle() = when (this){
    is AppException.UserException -> handle()
}

fun AppException.UserException.handle() = when (this){
    is AppException.UserException.UserNotFoundException -> ResponseEntity.notFound().build<Unit>()

}
