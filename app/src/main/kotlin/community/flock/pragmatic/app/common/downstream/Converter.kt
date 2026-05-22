package community.flock.pragmatic.app.common.downstream

import arrow.core.EitherNel
import arrow.core.raise.either
import community.flock.pragmatic.domain.error.MultipleValidationErrors
import community.flock.pragmatic.domain.error.SingleValidationError

/**
 * Placeholder interface to define downstream converters.
 * (i.e., used by publisher, adapter, or repository implementations)
 */
interface Converter<EXTERNAL : Any, DOMAIN : Any, EXTERNALIZED : Any> :
    Internalizer<EXTERNAL, DOMAIN>,
    Externalizer<DOMAIN, EXTERNALIZED>

interface Internalizer<in DTO : Any, out DOMAIN : Any> {
    fun DTO.internalize(): DOMAIN

    fun Iterable<DTO>.internalize(): List<DOMAIN> = map { it.internalize() }
}

interface Externalizer<in DOMAIN : Any, out DTO : Any> {
    fun DOMAIN.externalize(): DTO

    fun Iterable<DOMAIN>.externalize(): List<DTO> = map { it.externalize() }
}

interface SymmetricConverter<DTO : Any, DOMAIN : Any> : Converter<DTO, DOMAIN, DTO>

interface Verifier<DTO : Any, DOMAIN : Any> : Internalizer<DTO, EitherNel<SingleValidationError, DOMAIN>> {
    fun DTO.verify() = internalize().mapLeft { MultipleValidationErrors(it.toSet()) }

    fun Iterable<DTO>.verify() = either { map { it.verify().bind() } }
}

interface AsymmetricVerifier<EXTERNAL : Any, DOMAIN : Any, EXTERNALIZED : Any> :
    Verifier<EXTERNAL, DOMAIN>,
    Externalizer<DOMAIN, EXTERNALIZED>

interface SymmetricVerifier<DTO : Any, DOMAIN : Any> : AsymmetricVerifier<DTO, DOMAIN, DTO>
