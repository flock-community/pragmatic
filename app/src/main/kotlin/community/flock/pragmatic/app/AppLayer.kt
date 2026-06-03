package community.flock.pragmatic.app

import community.flock.pragmatic.app.user.database.LiveUserRepository
import community.flock.pragmatic.app.user.web.UserControllerDependencies
import community.flock.pragmatic.domain.user.UserService
import org.springframework.stereotype.Component

interface AppLayer : UserControllerDependencies

@Component
class LiveLayer(
    liveUserRepository: LiveUserRepository,
) : AppLayer {
    override val userService =
        object : UserService {
            override val userRepository = liveUserRepository
        }
}
