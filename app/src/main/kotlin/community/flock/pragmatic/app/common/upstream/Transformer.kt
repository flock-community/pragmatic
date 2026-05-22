package community.flock.pragmatic.app.common.upstream

import arrow.core.EitherNel
import community.flock.pragmatic.domain.error.MultipleValidationErrors
import community.flock.pragmatic.domain.error.SingleValidationError

/**
 * Placeholder interface to define upstream transformers.
 * (i.e., used by listener or controller implementations)
 */
interface Transformer<POTENTIAL : Any, DOMAIN : Any, PRODUCED : Any> :
    Consumer<POTENTIAL, DOMAIN>,
    Producer<DOMAIN, PRODUCED>

interface Consumer<in DTO : Any, out DOMAIN : Any> {
    fun DTO.consume(): DOMAIN

    fun Iterable<DTO>.consume(): List<DOMAIN> = map { it.consume() }
}

interface Producer<in DOMAIN : Any, out DTO : Any> {
    fun DOMAIN.produce(): DTO

    fun Iterable<DOMAIN>.produce(): List<DTO> = map { it.produce() }
}

interface SymmetricTransformer<DTO : Any, DOMAIN : Any> : Transformer<DTO, DOMAIN, DTO>

interface Validator<DTO : Any, DOMAIN : Any> : Consumer<DTO, EitherNel<SingleValidationError, DOMAIN>> {
    fun DTO.validate() = consume().mapLeft { MultipleValidationErrors(it.toSet()) }
}

interface AsymmetricValidator<POTENTIAL : Any, DOMAIN : Any, PRODUCED : Any> :
    Validator<POTENTIAL, DOMAIN>,
    Producer<DOMAIN, PRODUCED>

interface SymmetricValidator<DTO : Any, DOMAIN : Any> : AsymmetricValidator<DTO, DOMAIN, DTO>
