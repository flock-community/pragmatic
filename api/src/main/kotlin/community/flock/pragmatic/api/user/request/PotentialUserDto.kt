package community.flock.pragmatic.api.user.request

import io.swagger.annotations.ApiModelProperty

data class PotentialUserDto(
    @ApiModelProperty(required = true)
    val firstName: String,

    @ApiModelProperty(required = true)
    val lastName: String,

    @ApiModelProperty(required = true)
    val birthDay: String,
)
