package community.flock.pragmatic.app.wirespec

import community.flock.wirespec.Wirespec
import community.flock.wirespec.petstore.AddPetEndpoint
import community.flock.wirespec.petstore.FindPetsByStatusEndpoint
import community.flock.wirespec.petstore.GetPetByIdEndpoint
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils.toMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import kotlin.reflect.typeOf

interface PetstoreClient :
    AddPetEndpoint,
    GetPetByIdEndpoint,
    FindPetsByStatusEndpoint

@Component
class LivePetstoreClient(
    private val contentMapper: Wirespec.ContentMapper<String>,
    private val restTemplate: RestTemplate,
) : PetstoreClient {
    override suspend fun addPet(request: AddPetEndpoint.Request<*>) = handle(request, AddPetEndpoint::RESPONSE_MAPPER)

    override suspend fun findPetsByStatus(request: FindPetsByStatusEndpoint.Request<*>) =
        handle(request, FindPetsByStatusEndpoint::RESPONSE_MAPPER)

    override suspend fun getPetById(request: GetPetByIdEndpoint.Request<*>) = handle(request, GetPetByIdEndpoint::RESPONSE_MAPPER)

    private fun <Req : Wirespec.Request<*>, Res : Wirespec.Response<*>> handle(
        request: Req,
        responseMapper: (Wirespec.ContentMapper<String>) -> (Wirespec.Response<String>) -> Res,
    ): Res {
        val body = request.content?.let { contentMapper.write(it, typeOf<String>()) }?.body ?: ""
        val headers =
            request.headers
                .map { (k, v) -> k to v.map { it.toString() } }
                .toMap()
                .let(::toMultiValueMap)
        val res =
            restTemplate.exchange<String>(
                "https://6467e16be99f0ba0a819fd68.mockapi.io${request.path}",
                HttpMethod.valueOf(request.method.name),
                HttpEntity(body, headers),
            )
        val contentType = res.headers.contentType?.toString() ?: error("No content type")
        val content = res.body?.let { Wirespec.Content(contentType, it) }

        return responseMapper(contentMapper)(
            object : Wirespec.Response<String> {
                override val status = res.statusCode.value()
                override val headers = res.headers
                override val content = content
            },
        )
    }
}
