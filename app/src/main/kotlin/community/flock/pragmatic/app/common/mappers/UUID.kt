package community.flock.pragmatic.app.common.mappers

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.nel
import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.common.Producer
import community.flock.pragmatic.app.common.Validator
import community.flock.pragmatic.app.common.mappers.UUIDConsumer.validate
import community.flock.pragmatic.app.common.mappers.UUIDProducer.produce
import community.flock.pragmatic.app.exceptions.TechnicalException
import community.flock.pragmatic.domain.error.ValidationError.UUIDError
import java.util.UUID

object UUIDProducer : Producer<UUID, String> {
    override fun UUID.produce() = toString()
}

object UUIDConsumer : Validator<UUIDError, String, UUID> {
    override fun String.consume(): EitherNel<UUIDError, UUID> =
        Either
            .catch { UUID.fromString(this) }
            .mapLeft { UUIDError.nel() }
}

object UUIDExternalizer : Externalizer<UUID, String> {
    override fun UUID.externalize() = produce()
}

object UUIDInternalizer : Internalizer<String, Either<TechnicalException, UUID>> {
    override fun String.internalize() =
        validate()
            .mapLeft(TechnicalException::invoke)
}
