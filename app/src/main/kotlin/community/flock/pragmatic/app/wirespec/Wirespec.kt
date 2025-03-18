package community.flock.pragmatic.app.wirespec

import community.flock.wirespec.kotlin.Wirespec
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import kotlin.reflect.KType
import kotlin.reflect.full.createType

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
                    .body<String>(request.body ?: "")
            val response = client.exchange(request, String::class.java)

            response.run {
                Wirespec.RawResponse(
                    statusCode = statusCode.value(),
                    headers = headers.entries.associate { it.key to it.value },
                    body = body,
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
@Suppress("UNCHECKED_CAST")
object Serialization : Wirespec.Serialization<String>, Wirespec.ParamSerialization {
    override fun <T> serialize(
        t: T,
        kType: KType,
    ): String = Json.encodeToString(Json.serializersModule.serializer(kType), t)

    override fun <T> deserialize(
        raw: String,
        kType: KType,
    ): T =
        when (kType) {
            String::class.createType() -> raw as T
            else -> Json.decodeFromString(Json.serializersModule.serializer(kType), raw) as T
        }

    override fun <T> serializeParam(
        value: T,
        kType: KType,
    ): List<String> {
        TODO("Not yet implemented")
    }

    override fun <T> deserializeParam(
        values: List<String>,
        kType: KType,
    ): T {
        TODO("Not yet implemented")
    }
}
