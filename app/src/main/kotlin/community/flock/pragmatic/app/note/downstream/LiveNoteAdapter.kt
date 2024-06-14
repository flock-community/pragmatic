package community.flock.pragmatic.app.note.downstream

import community.flock.pragmatic.app.common.Externalizer
import community.flock.pragmatic.app.common.Internalizer
import community.flock.pragmatic.app.note.downstream.NoteExternalizer.externalize
import community.flock.pragmatic.app.note.downstream.NoteInternalizer.internalize
import community.flock.pragmatic.app.wirespec.NotesClient
import community.flock.pragmatic.domain.note.NoteAdapter
import community.flock.pragmatic.domain.note.model.Note
import community.flock.wirespec.generated.NoteDto
import community.flock.wirespec.generated.PotentialNoteDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class LiveNoteAdapter(
    private val restTemplate: RestTemplate,
    private val client: NotesClient,
) : NoteAdapter {
    override suspend fun saveNote(note: Note): Note {
        val potentialNote = note.externalize()
        val noteDto = saveNoteWithRestTemplate(potentialNote) ?: throw NullPointerException()
//        val noteDto = saveNoteWithWirespec(potentialNote)
        return noteDto.internalize()
    }

    private fun saveNoteWithRestTemplate(note: PotentialNoteDto) =
        restTemplate.postForObject("http://localhost/api/notes", note, NoteDto::class.java)

//    private suspend fun saveNoteWithWirespec(note: PotentialNoteDto): NoteDto {
//        val req = PostNoteEndpoint.RequestApplicationJson(note)
//        return when (val res = client.postNote(req)) {
//            is PostNoteEndpoint.Response200ApplicationJson -> res.content.body
//        }
//    }
}

object NoteExternalizer : Externalizer<Note, PotentialNoteDto> {
    override fun Note.externalize() =
        PotentialNoteDto(
            title = title,
            description = description,
        )
}

object NoteInternalizer : Internalizer<NoteDto, Note> {
    override fun NoteDto.internalize() =
        Note(
            id = id,
            title = title,
            description = description,
        )
}
