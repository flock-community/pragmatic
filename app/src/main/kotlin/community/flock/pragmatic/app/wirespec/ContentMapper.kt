package community.flock.pragmatic.app.wirespec

import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.wirespec.Wirespec
import org.springframework.stereotype.Component
import java.lang.reflect.Type

@Component
class ContentMapper(
    private val objectMapper: ObjectMapper,
) : Wirespec.ContentMapper<ByteArray> {
    override fun <T> read(
        content: Wirespec.Content<ByteArray>,
        valueType: Type,
    ): Wirespec.Content<T> =
        content.let {
            val type = objectMapper.constructType(valueType)
            val obj: T = objectMapper.readValue(content.body, type)
            Wirespec.Content(it.type, obj)
        }

    override fun <T> write(content: Wirespec.Content<T>): Wirespec.Content<ByteArray> =
        content.let {
            val bytes = objectMapper.writeValueAsBytes(content.body)
            Wirespec.Content(it.type, bytes)
        }
}
