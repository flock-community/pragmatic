package community.flock.pragmatic.domain.common

/**
 * Placeholder interface to define upstream transformers.
 * (i.e. used by resource or controller implementations)
 */
interface Transformer<DOMAIN : Any, EXTERNAL : Any> : Producer<DOMAIN, EXTERNAL>, Consumer<EXTERNAL, DOMAIN>

interface Producer<DOMAIN : Any, EXTERNAL : Any> {
    fun DOMAIN.produce(): EXTERNAL
}

interface Consumer<EXTERNAL : Any, DOMAIN : Any> {
    fun EXTERNAL.consume(): DOMAIN
}
