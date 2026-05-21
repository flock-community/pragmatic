package community.flock.pragmatic.app.user.database

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import community.flock.pragmatic.app.user.database.UserExternalizer.externalize
import community.flock.pragmatic.app.user.database.UserInternalizer.internalize
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class LiveUserRepository(
    private val repository: PostgresRepository,
) : UserRepository {
    override fun getAll(): Either<Error, List<User<User.Id.Valid>>> =
        either {
            repository
                .findAll()
                .map { it.internalize().bind() }
        }

    override fun getById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        either {
            val maybeUser =
                repository
                    .findById(userId.value)
                    .getOrNull()
            val user = ensureNotNull(maybeUser) { UserNotFound(userId) }
            user.internalize().bind()
        }

    override fun save(user: User<User.Id.NonExisting>): Either<Error, User<User.Id.Valid>> =
        either {
            user
                .externalize()
                .let(repository::save)
                .internalize()
                .bind()
        }

    override fun deleteById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        either {
            val user = getById(userId).bind()
            repository.deleteById(user.id.value)
            user
        }
}

interface PostgresRepository : JpaRepository<UserEntity, UUID>
