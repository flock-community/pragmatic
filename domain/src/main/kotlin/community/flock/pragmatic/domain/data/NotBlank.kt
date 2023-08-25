package community.flock.pragmatic.domain.data


@JvmInline
value class NotBlank private constructor(override val value: String):Value<String> {
    companion object {
        operator fun invoke(string: String) = string
            .takeIf { it.isNotBlank() }
            ?.let(::NotBlank)
    }
}
