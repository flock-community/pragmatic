package community.flock.pragmatic.api.user

import community.flock.pragmatic.api.common.API_BASE_URL
import community.flock.pragmatic.api.user.UserApi.Companion.USERS_PATH
import community.flock.pragmatic.api.user.request.PotentialUserDto
import community.flock.pragmatic.api.user.response.UserDto
import io.swagger.annotations.Api
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Api
@Path(USERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
interface UserApi {
    @GET
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class, responseContainer = "List"),
    )
    suspend fun getUsers(): List<UserDto>

    @GET
    @Path(BY_ID_PATH)
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class),
    )
    suspend fun getUserById(
        @PathParam("id") id: String,
    ): UserDto?

    @POST
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class),
    )
    suspend fun postUser(potentialUser: PotentialUserDto): UserDto

    @DELETE
    @Path(BY_ID_PATH)
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class),
    )
    suspend fun deleteUserById(
        @PathParam("id") id: String,
    ): UserDto?

    companion object {
        const val USERS_PATH = "$API_BASE_URL/users"
        const val BY_ID_PATH = "/{id}"
    }
}
