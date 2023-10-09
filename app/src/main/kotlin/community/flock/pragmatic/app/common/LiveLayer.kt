package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.user.downstream.LiveUserAdapter
import community.flock.pragmatic.app.user.upstream.UserControllerDependencies
import org.springframework.stereotype.Component

interface AppLayer : UserControllerDependencies

@Component
class LiveLayer : AppLayer {
    override val userAdapter = LiveUserAdapter()
}