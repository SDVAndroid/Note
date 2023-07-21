import org.junit.Test

import org.junit.Assert.*

class NotesServiceTest {

    @Test
    fun addNote() {
        val noteService = NotesService
        val title = "Тест"
        val text = "Тест на добавление заметки"
        val noteId = 1

        noteService.addNote(title, text, noteId)

        val addedNote = noteService.getNoteById(noteId)
        assertNotNull(addedNote)
        assertEquals(title, addedNote!!.title)
        assertEquals(text, addedNote.text)
        assertEquals(noteId, addedNote.noteId)
    }

    @Test
    fun createComment() {
        val noteService = NotesService
        val noteId = 1
        val message = "Пробный коммент"
        val commentId = 1

        noteService.createComment(noteId, message, commentId)

        val addedComment = noteService.databaseComments.find { it.commentId == commentId && it.noteId == noteId }
        assertNotNull(addedComment)
        assertEquals(message, addedComment?.message)
    }

    @Test
    fun editNote() {
        val noteService = NotesService
        val noteId = 1

        noteService.addNote("Тест", "Eto test", noteId)

        val newTitle = "BTest"
        val newText = "B test"

        noteService.editNote(noteId, newTitle, newText)

        val editedNote = noteService.getNoteById(noteId)
        assertNotNull(editedNote)
        assertEquals(newTitle, editedNote?.title)
        assertEquals(newText, editedNote?.text)
    }

    @Test
    fun testEditComment() {
        val notesService = NotesService
        val noteId = 1
        val title = "Тест"
        val text = "Тест на добавление заметки"

        notesService.addNote(title, text, noteId)

        val commentId = 1
        val message = "Comment"
        notesService.createComment(noteId, message, commentId)

        val newMessage = "Пробный коммент"
        notesService.editComment(commentId, newMessage, noteId)

        val editedComment = notesService.getCommentsByNoteId(noteId).find { it.commentId == commentId }
        assertEquals(newMessage, editedComment?.message)
    }

    @Test
    fun getNoteById() {
        val noteService = NotesService
        val noteId = 1
        noteService.addNote("Test", "Text zametki", noteId)

        val note = noteService.getNoteById(noteId)

        assertNotNull(note)
        assertEquals(noteId, note?.noteId)
    }

    @Test
    fun getCommentsByNoteId() {
        val noteService = NotesService
        val noteId = 1
        val comment1 = Comment("Komment 1", 1, noteId)
        val comment2 = Comment("Komment 2", 2, noteId)
        val comment3 = Comment("Komment 3", 3, noteId)
        noteService.databaseComments.addAll(listOf(comment1, comment2, comment3))

        val comments = noteService.getCommentsByNoteId(noteId)

        //assertEquals(3, comments.size)
        assertTrue(comments.contains(comment1))
        assertTrue(comments.contains(comment2))
        assertTrue(comments.contains(comment3))
    }

    @Test
    fun restoreComment() {
        val noteService = NotesService
        val commentId = 1
        val comment = Comment("Probnyi comment", commentId, 1)
        noteService.databaseDeleteComments.add(comment)

        noteService.restoreComment(commentId)

        val restoredComment = noteService.databaseComments.find { it.commentId == commentId }
        assertNotNull(restoredComment)
    }
}