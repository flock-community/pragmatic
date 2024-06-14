package community.flock.pragmatic.domain.data

interface Value<T : Any> {
    val value: T
}

operator fun <T : Any> Value<T>.component1() = value
