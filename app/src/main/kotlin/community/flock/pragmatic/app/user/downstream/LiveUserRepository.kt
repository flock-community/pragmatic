package community.flock.pragmatic.app.user.downstream

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import community.flock.pragmatic.app.common.catch
import community.flock.pragmatic.app.user.downstream.UserExternalizer.externalize
import community.flock.pragmatic.app.user.downstream.UserInternalizer.internalize
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class LiveUserRepository(private val repository: CassandraRepository) : UserRepository {
    override suspend fun getAll(): Either<Error, Flow<User<User.Id.Valid>>> =
        either {
            repository.findAll()
                .catch { asFlow() }.bind()
                .map { it.internalize().bind() }
        }

    override suspend fun getById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        either {
            val maybeUser =
                repository
                    .findById(userId.value)
                    .catch { awaitSingleOrNull() }
                    .bind()
            val user = ensureNotNull(maybeUser) { UserNotFound(userId) }
            user.internalize().bind()
        }

    override suspend fun save(user: User<User.Id.NonExisting>): Either<Error, User<User.Id.Valid>> =
        either {
            user.externalize()
                .let(repository::save)
                .catch { awaitSingle() }.bind()
                .internalize().bind()
        }

    override suspend fun deleteById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        either {
            val user = getById(userId).bind()
            repository
                .deleteById(user.id.value)
                .catch { awaitSingleOrNull() }.bind()
            user
        }
}

interface CassandraRepository : ReactiveCassandraRepository<UserEntity, UUID>
