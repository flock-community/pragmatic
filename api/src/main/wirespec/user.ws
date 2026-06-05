type UserIdentifier = String(/^[0-9a-f]{8}\b-[0-9a-f]{4}\b-[0-9a-f]{4}\b-[0-9a-f]{4}\b-[0-9a-f]{12}$/g)

type PotentialUserDto {
    firstName: String,
    `LastName`: String,
    birth-date: String
}

type UserDto {
    id : UserIdentifier,
    firstName: String,
    `LastName`: String,
    birth-date: String
}

type ErrorDto {
  reason: String
}

endpoint GetUsers GET /api/users -> {
    200 -> UserDto[]
    500 -> ErrorDto
}

endpoint GetUserById GET /api/users/{id: UserIdentifier} -> {
    200 -> UserDto
    500 -> ErrorDto
}

endpoint PostUser POST PotentialUserDto /api/users -> {
    200 -> UserDto
    500 -> ErrorDto
}

endpoint DeleteUser DELETE /api/users/{id: UserIdentifier} -> {
    200 -> UserDto
    404 -> ErrorDto
    500 -> ErrorDto
}
