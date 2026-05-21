package community.flock.pragmatic.app.environment

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.startupcheck.IsRunningStartupCheckStrategy
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.MountableFile.forClasspathResource

// Also update in docker compose yml
private const val POSTGRES_DOCKER_VERSION = "18"

internal interface WithPostgresContainer : WithContainer {
    companion object {
        private val postgresContainer: PostgreSQLContainer =
            PostgreSQLContainer("postgres:$POSTGRES_DOCKER_VERSION-alpine")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .withStartupCheckStrategy(IsRunningStartupCheckStrategy())
                .withEnvironment()
                .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgresContainer.connectionString }
        }
    }
}

private fun PostgreSQLContainer.withEnvironment() =
    withCopyFileToContainer(
        forClasspathResource("/postgres"),
        "/docker-entrypoint-initdb.d",
    )

private val PostgreSQLContainer.connectionString
    get() = "jdbc:postgresql://$host:${getMappedPort(5432)}/postgres"
