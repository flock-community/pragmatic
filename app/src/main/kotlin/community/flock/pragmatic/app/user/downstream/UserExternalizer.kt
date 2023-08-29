package community.flock.pragmatic.app.user.downstream

import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id

object UserExternalizer : Externalizer<User<Id.NonExisting>, User<Id.Valid>> {
    override fun User<Id.NonExisting>.externalize() = User(
        id = Id.Valid(hashCode()),
        firstName = firstName,
        lastName = lastName
    )
}
