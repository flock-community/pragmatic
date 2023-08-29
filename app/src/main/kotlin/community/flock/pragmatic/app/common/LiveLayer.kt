package community.flock.pragmatic.app.common

import community.flock.pragmatic.app.user.downstream.LiveUserAdapter
import community.flock.pragmatic.domain.AppLayer
import org.springframework.stereotype.Component

@Component
data class LiveLayer(
    override val userAdapter: LiveUserAdapter
) : AppLayer
