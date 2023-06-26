package community.flock.pragmatic.api.user.response

import io.swagger.annotations.ApiModelProperty

data class UserDto(
    @ApiModelProperty(required = true)
    val id: String,

    @ApiModelProperty(required = true)
    val firstName: String,

    @ApiModelProperty(required = true)
    val lastName: String,
)
