package community.flock.pragmatic.app.user.web

import community.flock.pragmatic.api.user.response.UserDto
import community.flock.pragmatic.app.common.Producer
import community.flock.pragmatic.app.user.web.UserProducer.produce
import community.flock.pragmatic.domain.user.model.User
import community.flock.pragmatic.domain.user.model.User.Id
import community.flock.wirespec.generated.UserIdentifier
import community.flock.wirespec.generated.WsUserDto
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

object UsersProducer : Producer<List<User<Id.Valid>>, List<UserDto>> {
    override fun List<User<Id.Valid>>.produce() = map { it.produce() }
}

object UserProducer : Producer<User<Id.Valid>, UserDto> {
    override fun User<Id.Valid>.produce() =
        UserDto(
            id = id.value.toString(),
            firstName = firstName.value,
            lastName = lastName.value,
            birthDay = birthDay.value.format(ISO_LOCAL_DATE),
        )
}

object WsUsersProducer : Producer<List<User<Id.Valid>>, List<WsUserDto>> {
    override fun List<User<Id.Valid>>.produce() =
        with(WsUserProducer) {
            map { it.produce() }
        }
}

object WsUserProducer : Producer<User<Id.Valid>, WsUserDto> {
    override fun User<Id.Valid>.produce() =
        WsUserDto(
            id = UserIdentifier(id.value.toString()),
            firstName = firstName.value,
            lastName = lastName.value,
            birthDate = birthDay.value.format(ISO_LOCAL_DATE),
        )
}
