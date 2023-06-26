package community.flock.pragmatic.api.user

import community.flock.pragmatic.api.common.apiBaseUrl
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
@Path(apiBaseUrl)
@Produces(MediaType.APPLICATION_JSON)
interface UserApi {

    @GET
    @Path(path)
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class, responseContainer = "List")
    )
    suspend fun getUsers(): List<UserDto>

    @GET
    @Path("$path/{id}")
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class)
    )
    suspend fun getUserById(@PathParam("id") id: String): UserDto?

    @POST
    @Path(path)
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class)
    )
    suspend fun postUser(user: PotentialUserDto): UserDto

    @DELETE
    @Path("$path/{id}")
    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = UserDto::class)
    )
    suspend fun deleteUserById(@PathParam("id") id: String): UserDto?

    companion object {
        const val path = "/user"
    }

}
