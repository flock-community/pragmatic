package community.flock.pragmatic.app.common

import arrow.core.Either
import community.flock.pragmatic.domain.error.TechnicalError

suspend fun <T, R> T.catch(block: suspend T.() -> R) =
    Either.catch { block() }
        .mapLeft(TechnicalError::invoke)
