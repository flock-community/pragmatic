package community.flock.pragmatic.app.user.downstream

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID

@Table("users")
data class UserEntity(
    @PrimaryKey("user_id")
    val userId: UUID,
    @field:Column("first_name")
    val firstName: String,
    @field:Column("last_name")
    val lastName: String,
    @field:Column("birth_day")
    val birthDay: String,
)
