package community.flock.pragmatic.app.wirespec

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.wirespec.Wirespec
import org.springframework.stereotype.Component
import kotlin.reflect.KType
import kotlin.reflect.javaType

@Component
@OptIn(ExperimentalStdlibApi::class)
class ContentMapper(
    private val objectMapper: ObjectMapper,
) : Wirespec.ContentMapper<String> {
    override fun <T> read(
        content: Wirespec.Content<String>,
        valueType: KType,
    ): Wirespec.Content<T> =
        content.let {
            val type = objectMapper.constructType(valueType.javaType)
            val obj: T = objectMapper.readValue(content.body, type)
            Wirespec.Content(it.type, obj)
        }

    override fun <T> write(
        content: Wirespec.Content<T>,
        valueType: KType,
    ): Wirespec.Content<String> =
        content.let {
            val bytes = objectMapper.writeValueAsString(content.body)
            Wirespec.Content(it.type, bytes)
        }
}
