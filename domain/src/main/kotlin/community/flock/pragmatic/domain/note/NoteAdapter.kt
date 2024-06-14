package community.flock.pragmatic.domain.note

import community.flock.pragmatic.domain.note.model.Note

interface NoteAdapter {
    suspend fun saveNote(note: Note): Note
}

interface HasNoteAdapter {
    val noteAdapter: NoteAdapter
}
