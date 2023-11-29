package community.flock.pragmatic.domain.pet

import community.flock.pragmatic.domain.pet.model.Pet

interface PetAdapter {
    suspend fun getPetById(id: Int): Pet

    suspend fun savePet(pet: Pet): Pet
}
