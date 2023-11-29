package community.flock.pragmatic.app.wirespec

import community.flock.wirespec.Wirespec
import community.flock.wirespec.petstore.AddPet
import community.flock.wirespec.petstore.FindPetsByStatus
import community.flock.wirespec.petstore.GetPetById
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI

interface PetstoreClient : AddPet, GetPetById, FindPetsByStatus

@Component
class LivePetstoreClient(
    private val contentMapper: Wirespec.ContentMapper<ByteArray>,
    private val restTemplate: RestTemplate,
) : PetstoreClient {

    override suspend fun addPet(request: AddPet.Request<*>) =
        handle(request, AddPet::RESPONSE_MAPPER)

    override suspend fun findPetsByStatus(request: FindPetsByStatus.Request<*>) =
        handle(request, FindPetsByStatus::RESPONSE_MAPPER)

    override suspend fun getPetById(request: GetPetById.Request<*>) =
        handle(request, GetPetById::RESPONSE_MAPPER)

    private fun <Req : Wirespec.Request<*>, Res : Wirespec.Response<*>> handle(
        request: Req,
        responseMapper: (Wirespec.ContentMapper<ByteArray>) -> (Wirespec.Response<ByteArray>) -> Res
    ) = restTemplate.execute(
        URI("https://6467e16be99f0ba0a819fd68.mockapi.io${request.path}"),
        HttpMethod.valueOf(request.method.name),
        { req ->
            request.content
                ?.let { contentMapper.write(it) }
                ?.let { req.body.write(it.body) }
        },
        { res ->
            val contentType = res.headers.contentType?.toString() ?: error("No content type")
            val content = Wirespec.Content(contentType, res.body.readBytes())
            val response = object : Wirespec.Response<ByteArray> {
                override val status = res.statusCode.value()
                override val headers = res.headers
                override val content = content
            }
            responseMapper(contentMapper)(response)
        }
    ) ?: error("No response")
}
