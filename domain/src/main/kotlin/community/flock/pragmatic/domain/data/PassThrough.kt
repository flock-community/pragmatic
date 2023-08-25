package community.flock.pragmatic.domain.data

@JvmInline
value class PassThrough<T : Any> private constructor(override val value: T) : Value<T> {
    companion object {
        operator fun <T : Any> invoke(t: T?) = t?.let(::PassThrough)
    }
}
