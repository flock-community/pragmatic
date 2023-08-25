package community.flock.pragmatic.app.common

sealed class Either<A, B> {

    class Left<A>(val value: A) : Either<A, Nothing>()
    class Right<B>(val value: B) : Either<Nothing, B>()

}
