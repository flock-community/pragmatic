package community.flock.pragmatic.domain.error

operator fun ValidationError.plus(other: ValidationError): MultipleValidationErrors = MultipleValidationErrors(errors + other.errors)

val ValidationError.errors: Set<SingleValidationError>
    get() =
        when (this) {
            is SingleValidationError -> setOf(this)
            is MultipleValidationErrors -> errors
        }
