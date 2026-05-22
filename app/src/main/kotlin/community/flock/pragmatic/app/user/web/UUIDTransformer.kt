package community.flock.pragmatic.app.user.web

import arrow.core.Either.Companion.catch
import arrow.core.EitherNel
import arrow.core.nel
import community.flock.pragmatic.app.common.upstream.SymmetricValidator
import community.flock.pragmatic.domain.error.UUIDError
import java.util.UUID

object UUIDTransformer : SymmetricValidator<String, UUID> {
    override fun String.consume(): EitherNel<UUIDError, UUID> =
        catch { UUID.fromString(this) }
            .mapLeft { UUIDError.nel() }

    override fun UUID.produce(): String = toString()
}
