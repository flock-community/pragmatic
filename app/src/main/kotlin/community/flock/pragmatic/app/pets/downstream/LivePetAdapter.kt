package community.flock.pragmatic.app.pets.downstream

import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.pets.downstream.WsPetExternalizer.externalize
import community.flock.pragmatic.app.pets.downstream.WsPetInternalizer.internalize
import community.flock.pragmatic.app.wirespec.PetstoreClient
import community.flock.pragmatic.domain.pet.PetAdapter
import community.flock.pragmatic.domain.pet.model.Pet
import community.flock.wirespec.petstore.AddPet
import community.flock.wirespec.petstore.GetPetById
import community.flock.wirespec.petstore.PetStatus
import org.springframework.stereotype.Component
import community.flock.wirespec.petstore.Pet as ExternalPet

@Component
class LivePetAdapter(private val client: PetstoreClient) : PetAdapter {

    override suspend fun getPetById(id: Int): Pet = run {

        val req = GetPetById.RequestUnit(id)
        when (val res = client.getPetById(req)) {
            is GetPetById.Response200ApplicationJson -> res.content.body
            is GetPetById.Response200ApplicationXml -> error("Something went wrong")
            is GetPetById.Response400Unit -> error("Something went wrong")
            is GetPetById.Response404Unit -> error("Something went wrong")
        }.internalize()
    }

    override suspend fun savePet(pet: Pet) = run {
        val req = AddPet.RequestApplicationJson(pet.externalize())
        val savedPet = when (val res = client.addPet(req)) {
            is AddPet.Response200ApplicationJson -> res.content.body
            is AddPet.Response200ApplicationXml -> res.content.body
            is AddPet.Response405Unit -> error("Something went wrong")
        }
        savedPet.internalize()
    }
}

object WsPetInternalizer : Internalizer<ExternalPet, Pet> {
    override fun ExternalPet.internalize() = Pet(
        id = id,
        name = name,
        status = status?.toString() ?: PetStatus.pending.toString()
    )
}

object WsPetExternalizer : Externalizer<Pet, ExternalPet> {
    override fun Pet.externalize() = ExternalPet(
        id = id,
        name = name,
        status = PetStatus.valueOf(status),
        photoUrls = emptyList()
    )
}
