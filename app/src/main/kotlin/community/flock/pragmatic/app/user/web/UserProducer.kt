package community.flock.pragmatic.app.user.web

import community.flock.pragmatic.api.wirespec.UserDto
import community.flock.pragmatic.api.wirespec.UserIdentifier
import community.flock.pragmatic.app.common.Producer
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

object UsersProducer : Producer<List<User<Id.Valid>>, List<UserDto>> {
    override fun List<User<Id.Valid>>.produce() =
        with(UserProducer) {
            map { it.produce() }
        }
}

object UserProducer : Producer<User<Id.Valid>, UserDto> {
    override fun User<Id.Valid>.produce() =
        UserDto(
            id = UserIdentifier(id.value.toString()),
            firstName = firstName.value,
            lastName = lastName.value,
            birthDate = birthDay.value.format(ISO_LOCAL_DATE),
        )
}
