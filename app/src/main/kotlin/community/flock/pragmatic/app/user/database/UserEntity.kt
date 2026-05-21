package community.flock.pragmatic.app.user.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val birthDay: String,
)
