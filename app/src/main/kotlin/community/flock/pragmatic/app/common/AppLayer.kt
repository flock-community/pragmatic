package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.user.downstream.LiveUserAdapter
import community.flock.pragmatic.domain.user.HasUserAdapter
import org.springframework.stereotype.Component

interface AppLayer : HasUserAdapter

@Component
class LiveLayer : AppLayer {
    override val userAdapter = LiveUserAdapter()
}
