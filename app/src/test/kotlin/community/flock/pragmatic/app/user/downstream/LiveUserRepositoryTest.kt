package community.flock.pragmatic.app.user.downstream

import arrow.core.getOrElse
import com.datastax.oss.driver.api.core.CqlSession
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.app.environment.CASSANDRA_DOCKER_VERSION
import community.flock.pragmatic.app.user.upstream.UserConsumer.validate
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class LiveUserRepositoryTest {
    @Autowired
    private lateinit var userRepository: LiveUserRepository

    @Nested
    inner class Docker {
        @Test
        fun testInstance() {
            cassandra.isRunning shouldBe true
        }
    }

    @Nested
    inner class Repository {
        @Test
        fun testLiveUserRepository(): Unit =
            runBlocking {
                val user =
                    PotentialUserDto(
                        firstName = "FirstName",
                        lastName = "LastName",
                        birthDay = "2020-01-01",
                    ).validate().getOrElse { throw it }
                userRepository.save(user)
                userRepository.getAll().shouldBeRight().toList().run {
                    size shouldBe 1
                    first().run {
                        firstName.value shouldBe "FirstName"
                        lastName.value shouldBe "LastName"
                    }
                }
            }
    }

    companion object {
        private const val KEYSPACE_NAME = "pragmatic"
        private const val CREATE_KEYSPACE_QUERY =
            """CREATE KEYSPACE IF NOT EXISTS $KEYSPACE_NAME WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};"""

        @Container
        @ServiceConnection
        private val cassandra = CassandraContainer("cassandra:$CASSANDRA_DOCKER_VERSION")

        @JvmStatic
        @BeforeAll
        fun setupCassandraConnectionProperties() {
            System.setProperty("spring.cassandra.schema-action", "create_if_not_exists")
            System.setProperty("spring.cassandra.keyspace-name", KEYSPACE_NAME)
            System.setProperty("spring.cassandra.contact-points", cassandra.host)
            System.setProperty("spring.cassandra.port", cassandra.getMappedPort(9042).toString())
            session().execute(CREATE_KEYSPACE_QUERY)
        }

        private fun session() =
            CqlSession.builder()
                .addContactPoint(cassandra.contactPoint)
                .withLocalDatacenter(cassandra.localDatacenter)
                .build()
    }
}
