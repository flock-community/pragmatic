package community.flock.pragmatic.domain.user

import arrow.core.Either
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAll(): Either<Error, Flow<User<Id.Valid>>>

    suspend fun getById(userId: Id.Valid): Either<Error, User<Id.Valid>>

    suspend fun save(user: User<Id.NonExisting>): Either<Error, User<Id.Valid>>

    suspend fun deleteById(userId: Id.Valid): Either<Error, User<Id.Valid>>
}

interface HasUserRepository {
    val userRepository: UserRepository
}
