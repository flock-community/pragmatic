package community.flock.pragmatic.app.wirespec

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.wirespec.integration.jackson.kotlin.WirespecModuleKotlin
import community.flock.wirespec.kotlin.Wirespec
import community.flock.wirespec.kotlin.serde.DefaultParamSerialization
import community.flock.wirespec.kotlin.serde.DefaultPathSerialization
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.client.exchange
import kotlin.reflect.KType
import kotlin.reflect.javaType

@Component
class WirespecClient(
    private val client: RestOperations,
) {
    fun handle(request: Wirespec.RawRequest): Wirespec.RawResponse =
        runBlocking {
            val request =
                RequestEntity
                    .method(HttpMethod.valueOf(request.method), request.path.joinToString(""))
                    .headers { headers -> request.headers.forEach { (name, values) -> headers.addAll(name, values) } }
                    .body(request.body?.toString(Charsets.UTF_8) ?: "")
            val response = client.exchange<String>(request)

            response.run {
                Wirespec.RawResponse(
                    statusCode = statusCode.value(),
                    headers = headers.toSingleValueMap().entries.associate { it.key to listOf(it.value) },
                    body = body?.toByteArray(Charsets.UTF_8),
                )
            }
        }
}

/**
 * Example implementation of Wirespec Serialization using DefaultParamSerialization
 * This class handles standard parameter serialization for headers and query parameters.
 * For custom serialization requirements, you can create your own implementation
 * of Wirespec.ParamSerialization instead of using DefaultParamSerialization.
 * In this case, you don't need the dependency on community.flock.wirespec.integration:wirespec
 */
@Component
class Serialization(
    objectMapper: ObjectMapper,
) : Wirespec.Serialization,
    Wirespec.ParamSerialization by DefaultParamSerialization(),
    Wirespec.PathSerialization by DefaultPathSerialization() {
    private val wirespecObjectMapper =
        objectMapper
            .copy()
            .registerModule(WirespecModuleKotlin())

    override fun <T : Any> serializeBody(
        t: T,
        kType: KType,
    ): ByteArray =
        when (t) {
            is String -> t.toByteArray()
            else -> wirespecObjectMapper.writeValueAsBytes(t)
        }

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalStdlibApi::class)
    override fun <T : Any> deserializeBody(
        raw: ByteArray,
        kType: KType,
    ): T =
        when {
            kType.classifier == String::class -> {
                raw as T
            }

            else -> {
                wirespecObjectMapper
                    .constructType(kType.javaType)
                    .let { wirespecObjectMapper.readValue(raw, it) }
            }
        }
}
