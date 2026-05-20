package community.flock.pragmatic.app.environment

import com.datastax.oss.driver.api.core.CqlSession
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.cassandra.CassandraContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

internal interface WithCassandraContainer : WithContainer {
    companion object {
        private const val KEYSPACE_NAME = "pragmatic"
        private const val CREATE_KEYSPACE_QUERY =
            """CREATE KEYSPACE IF NOT EXISTS $KEYSPACE_NAME WITH replication = {'class':'SimpleStrategy','replication_factor':'1'};"""

        @Container
        @ServiceConnection
        val cassandra =
            "cassandra:$CASSANDRA_DOCKER_VERSION"
                .let(DockerImageName::parse)
                .let(::CassandraContainer)
                .apply { start() }

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
            CqlSession
                .builder()
                .addContactPoint(cassandra.contactPoint)
                .withLocalDatacenter(cassandra.localDatacenter)
                .build()
    }

//    companion object {
//        private val postgresContainer =
//            CassandraContainer("postgres:18-alpine")
//                .withDatabaseName("postgres")
//                .withUsername("postgres")
//                .withPassword("postgres")
//                .withStartupCheckStrategy(IsRunningStartupCheckStrategy())
//                .withEnvironment()
//                .apply { start() }
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun registerDBContainer(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url") { postgresContainer.connectionString }
//        }
//    }
}

// private fun PostgreSQLContainer.withEnvironment() =
//    withCopyFileToContainer(
//        forClasspathResource("/sql"),
//        "/docker-entrypoint-initdb.d",
//    )
//
// private val PostgreSQLContainer.connectionString
//    get() = "jdbc:postgresql://$host:${getMappedPort(5432)}/postgres"
