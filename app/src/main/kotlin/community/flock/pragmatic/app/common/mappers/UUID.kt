package community.flock.pragmatic.app.common.mappers

import arrow.core.Either
import community.flock.pragmatic.app.common.Consumer
import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.common.Producer
import community.flock.pragmatic.app.common.mappers.UUIDConsumer.consume
import community.flock.pragmatic.app.common.mappers.UUIDProducer.produce
import community.flock.pragmatic.app.exceptions.BusinessException
import community.flock.pragmatic.app.exceptions.InvalidUUID
import community.flock.pragmatic.app.exceptions.TechnicalException
import java.util.UUID

object UUIDProducer : Producer<UUID, String> {
    override fun UUID.produce() = toString()
}

object UUIDConsumer : Consumer<String, Either<BusinessException, UUID>> {
    override fun String.consume() = Either.catch { UUID.fromString(this) }
        .mapLeft { InvalidUUID() }
}

object UUIDExternalizer : Externalizer<UUID, String> {
    override fun UUID.externalize() = produce()
}

object UUIDInternalizer : Internalizer<String, Either<TechnicalException, UUID>> {
    override fun String.internalize() = consume()
        .mapLeft(TechnicalException::invoke)
}
