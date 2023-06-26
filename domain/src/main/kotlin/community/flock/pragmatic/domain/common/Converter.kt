package community.flock.pragmatic.domain.common

/**
 * Placeholder interface to define downstream converters.
 * (i.e. used by adapter or repository implementations)
 */
interface Converter<DOMAIN : Any, EXTERNAL : Any> : Externalizer<DOMAIN, EXTERNAL>, Internalizer<EXTERNAL, DOMAIN>

interface Externalizer<DOMAIN : Any, EXTERNAL : Any> {
    fun DOMAIN.externalize(): EXTERNAL
}

interface Internalizer<EXTERNAL : Any, DOMAIN : Any> {
    fun EXTERNAL.internalize(): DOMAIN
}
