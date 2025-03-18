package community.flock.pragmatic.app.pets.downstream

import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.pets.downstream.WsPetExternalizer.externalize
import community.flock.pragmatic.app.pets.downstream.WsPetInternalizer.internalize
import community.flock.pragmatic.app.wirespec.PetstoreClient
import community.flock.pragmatic.domain.pet.PetAdapter
import community.flock.pragmatic.domain.pet.model.Pet
import community.flock.wirespec.petstore.AddPetEndpoint
import community.flock.wirespec.petstore.GetPetByIdEndpoint
import community.flock.wirespec.petstore.PetStatus
import org.springframework.stereotype.Component
import community.flock.wirespec.petstore.Pet as ExternalPet

@Component
class LivePetAdapter(
    private val client: PetstoreClient,
) : PetAdapter {
    override suspend fun getPetById(id: Long): Pet =
        when (val res = client.getPetById(GetPetByIdEndpoint.Request(id))) {
            is GetPetByIdEndpoint.Response200 -> res.body
            is GetPetByIdEndpoint.Response400 -> error("Something went wrong")
            is GetPetByIdEndpoint.Response404 -> error("Something went wrong")
        }.internalize()

    override suspend fun savePet(pet: Pet) =
        when (val res = client.addPet(AddPetEndpoint.Request(pet.externalize()))) {
            is AddPetEndpoint.Response200 -> res.body
            is AddPetEndpoint.Response405 -> error("Something went wrong")
        }.internalize()
}

object WsPetInternalizer : Internalizer<ExternalPet, Pet> {
    override fun ExternalPet.internalize() =
        Pet(
            id = id,
            name = name,
            status = status?.toString() ?: PetStatus.pending.toString(),
        )
}

object WsPetExternalizer : Externalizer<Pet, ExternalPet> {
    override fun Pet.externalize() =
        ExternalPet(
            id = id,
            name = name,
            status = PetStatus.valueOf(status),
            photoUrls = emptyList(),
            category = null,
            tags = null,
        )
}
