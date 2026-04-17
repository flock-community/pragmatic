package community.flock.pragmatic.app.wirespec

import community.flock.wirespec.petstore.endpoint.AddPet
import community.flock.wirespec.petstore.endpoint.FindPetsByStatus
import community.flock.wirespec.petstore.endpoint.GetPetById
import org.springframework.stereotype.Component

interface PetstoreClient :
    AddPet.Handler,
    GetPetById.Handler,
    FindPetsByStatus.Handler

@Component
class LivePetstoreClient(
    private val client: WirespecClient,
    private val serialization: Serialization,
) : PetstoreClient {
    override suspend fun addPet(request: AddPet.Request) =
        with(AddPet.Handler.client(serialization)) {
            to(request).let(client::handle).let(::from)
        }

    override suspend fun getPetById(request: GetPetById.Request) =
        with(GetPetById.Handler.client(serialization)) {
            to(request).let(client::handle).let(::from)
        }

    override suspend fun findPetsByStatus(request: FindPetsByStatus.Request) =
        with(FindPetsByStatus.Handler.client(serialization)) {
            to(request).let(client::handle).let(::from)
        }
}
