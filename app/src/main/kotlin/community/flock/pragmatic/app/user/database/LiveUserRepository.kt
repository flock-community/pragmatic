package community.flock.pragmatic.app.user.database

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import community.flock.pragmatic.app.user.database.UserConverter.externalize
import community.flock.pragmatic.app.user.database.UserConverter.verify
import community.flock.pragmatic.domain.error.DomainError
import community.flock.pragmatic.domain.error.Error
import community.flock.pragmatic.domain.error.UserNotFound
import community.flock.pragmatic.domain.user.UserRepository
import community.flock.pragmatic.domain.user.model.User
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class ExposedDatabase(
    env: Environment,
) {
    val postgres =
        Database
            .connect(
                url = env.getRequiredProperty("spring.datasource.url"),
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "postgres",
            )
}

fun <E : Error, T : Any> Database.handle(block: () -> Either<E, T>) =
    either {
        transaction(this@handle) { block() }.bind()
    }

fun <D : DomainError, E : Error, T : Any> Database.handle(
    domainError: D,
    block: () -> Either<E, T>?,
) = either {
    val maybe = transaction(this@handle) { block() }?.bind()
    ensureNotNull(maybe) { domainError }
}

@Repository
class LiveUserRepository(
    private val db: ExposedDatabase,
) : UserRepository {
    override fun getAll(): Either<Error, List<User<User.Id.Valid>>> =
        db.postgres.handle {
            UserEntity.all().verify()
        }

    override fun getById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        db.postgres.handle(UserNotFound(userId)) {
            UserEntity.findById(userId.value)?.verify()
        }

    override fun save(user: User<User.Id.Absent>): Either<Error, User<User.Id.Valid>> =
        db.postgres.handle {
            user.externalize().verify()
        }

    override fun deleteById(userId: User.Id.Valid): Either<Error, User<User.Id.Valid>> =
        db.postgres.handle(UserNotFound(userId)) {
            UserEntity.findById(userId.value)?.also { it.delete() }?.verify()
        }
}
