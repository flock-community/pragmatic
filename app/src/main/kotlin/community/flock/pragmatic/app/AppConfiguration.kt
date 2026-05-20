package community.flock.pragmatic.app

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfiguration {
    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun objectMapper() =
        ObjectMapper().apply {
            registerKotlinModule()
            registerModule(Jdk8Module())
            registerModule(JavaTimeModule())
            setDefaultPropertyInclusion(NON_ABSENT)
            disable(WRITE_DATES_AS_TIMESTAMPS)
            disable(FAIL_ON_UNKNOWN_PROPERTIES)
            enable(FAIL_ON_NULL_FOR_PRIMITIVES)
        }
}
