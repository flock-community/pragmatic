package community.flock.pragmatic.app.user.database

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

const val MAX_VARCHAR_LENGTH = 128

object Users : UUIDTable("users") {
    val firstName = varchar("firstname", MAX_VARCHAR_LENGTH)
    val lastName = varchar("lastname", MAX_VARCHAR_LENGTH)
    val birthDay = varchar("birthday", MAX_VARCHAR_LENGTH)
}

class UserEntity(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(Users)

    var firstName by Users.firstName
    var lastName by Users.lastName
    var birthDay by Users.birthDay

    override fun toString(): String = "User(id=$id, firstName=$firstName, lastName=$lastName, birthDay=$birthDay)"
}
