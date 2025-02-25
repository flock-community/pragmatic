package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.user.web.UserControllerDependencies
import community.flock.pragmatic.domain.user.UserRepository
import org.springframework.stereotype.Component

interface AppLayer : UserControllerDependencies

@Component
class LiveLayer(
    override val userRepository: UserRepository,
) : AppLayer
