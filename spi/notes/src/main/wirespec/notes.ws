type PotentialNoteDto {
    title: String,
    description: String
}

type NoteDto {
    id: Integer,
    title: String,
    description: String
}

endpoint PostNote POST PotentialNoteDto /api/notes -> {
    200 -> NoteDto
}
