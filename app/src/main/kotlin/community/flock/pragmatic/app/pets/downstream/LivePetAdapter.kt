package community.flock.pragmatic.app.pets.downstream

import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.pets.downstream.PetExternalizer.externalize
import community.flock.pragmatic.app.pets.downstream.PetInternalizer.internalize
import community.flock.pragmatic.app.wirespec.PetstoreClient
import community.flock.pragmatic.domain.pet.PetAdapter
import community.flock.pragmatic.domain.pet.model.Pet
import community.flock.wirespec.petstore.endpoint.AddPet
import community.flock.wirespec.petstore.endpoint.GetPetById
import community.flock.wirespec.petstore.model.PetStatus
import org.springframework.stereotype.Component
import community.flock.wirespec.petstore.model.Pet as ExternalPet

@Component
class LivePetAdapter(
    private val client: PetstoreClient,
) : PetAdapter {
    override suspend fun getPetById(id: Long): Pet =
        when (val res = client.getPetById(GetPetById.Request(id))) {
            is GetPetById.Response200 -> res.body
            is GetPetById.Response400 -> error("Something went wrong")
            is GetPetById.Response404 -> error("Something went wrong")
        }.internalize()

    override suspend fun savePet(pet: Pet) =
        when (val res = client.addPet(AddPet.Request(pet.externalize()))) {
            is AddPet.Response200 -> res.body
            is AddPet.Response405 -> error("Something went wrong")
        }.internalize()
}

object PetInternalizer : Internalizer<ExternalPet, Pet> {
    override fun ExternalPet.internalize() =
        Pet(
            id = id,
            name = name,
            status = status?.toString() ?: PetStatus.pending.toString(),
        )
}

object PetExternalizer : Externalizer<Pet, ExternalPet> {
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
