package community.flock.pragmatic.app.wirespec

import community.flock.wirespec.petstore.AddPetEndpoint
import community.flock.wirespec.petstore.FindPetsByStatusEndpoint
import community.flock.wirespec.petstore.GetPetByIdEndpoint
import org.springframework.stereotype.Component

interface PetstoreClient :
    AddPetEndpoint.Handler,
    GetPetByIdEndpoint.Handler,
    FindPetsByStatusEndpoint.Handler

@Component
class LivePetstoreClient(
    private val client: WirespecClient,
) : PetstoreClient {
    override suspend fun addPet(request: AddPetEndpoint.Request) =
        with(AddPetEndpoint.Handler.client(Serialization)) {
            to(request).let(client::handle).let(::from)
        }

    override suspend fun getPetById(request: GetPetByIdEndpoint.Request) =
        with(GetPetByIdEndpoint.Handler.client(Serialization)) {
            to(request).let(client::handle).let(::from)
        }

    override suspend fun findPetsByStatus(request: FindPetsByStatusEndpoint.Request) =
        with(FindPetsByStatusEndpoint.Handler.client(Serialization)) {
            to(request).let(client::handle).let(::from)
        }
}
