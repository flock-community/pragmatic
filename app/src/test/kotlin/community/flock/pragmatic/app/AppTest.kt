package community.flock.pragmatic.app

import community.flock.pragmatic.app.environment.WithCassandraContainer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AppTest : WithCassandraContainer {
    @Test
    fun contextLoads() {
    }
}
