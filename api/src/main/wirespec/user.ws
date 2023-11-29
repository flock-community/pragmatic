refined UserIdentifier /^[0-9a-f]{8}\b-[0-9a-f]{4}\b-[0-9a-f]{4}\b-[0-9a-f]{4}\b-[0-9a-f]{12}$/g

type WsPotentialUserDto {
    firstName: String,
    lastName: String,
    birthDate: String
}

type WsUserDto {
    id : UserIdentifier,
    firstName: String,
    lastName: String,
    birthDate: String
}

type WsErrorDto {
  reason: String
}

endpoint GetUsers GET /api/users -> {
    200 -> WsUserDto[]
    500 -> WsErrorDto
}

endpoint GetUserById GET /api/users/{id: UserIdentifier} -> {
    200 -> WsUserDto
    500 -> WsErrorDto
}

endpoint PostUser POST WsPotentialUserDto /api/users -> {
    200 -> WsUserDto
    500 -> WsErrorDto
}

endpoint DeleteUser DELETE /api/users/{id: UserIdentifier} -> {
    200 -> WsUserDto
    404 -> WsErrorDto
    500 -> WsErrorDto
}
