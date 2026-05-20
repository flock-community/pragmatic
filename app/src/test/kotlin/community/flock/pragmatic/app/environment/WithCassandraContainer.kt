package community.flock.pragmatic.app.environment

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.cassandra.CassandraContainer
import org.testcontainers.utility.MountableFile.forClasspathResource

internal interface WithCassandraContainer : WithContainer {
    companion object {
        private const val KEYSPACE_NAME = "pragmatic"

        val cassandra: CassandraContainer =
            CassandraContainer("cassandra:$CASSANDRA_DOCKER_VERSION")
                .withEnvironment()
                .apply {
                    start()
                    execInContainer("sh", "db-init.sh")
                }

        @JvmStatic
        @DynamicPropertySource
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.cassandra.schema-action") { "create_if_not_exists" }
            registry.add("spring.cassandra.keyspace-name") { KEYSPACE_NAME }
            registry.add("spring.cassandra.contact-points") { cassandra.host }
            registry.add("spring.cassandra.port") { cassandra.getMappedPort(9042).toString() }
        }
    }
}

private fun CassandraContainer.withEnvironment(): CassandraContainer =
    withCopyFileToContainer(
        forClasspathResource("/database"),
        "/custom-init",
    ).apply { workingDirectory = "/custom-init" }
