package community.flock.pragmatic.app.environment

import org.testcontainers.junit.jupiter.EnabledIfDockerAvailable
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@EnabledIfDockerAvailable
interface WithContainer
