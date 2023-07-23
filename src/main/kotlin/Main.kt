data class Notes(
    var title: String,
    var text: String,
    val noteId: Int,
)

data class Comment(
    var message: String,
    val commentId: Int,
    val noteId: Int
)

class NoteNotFoundException(noteId: Int) : Exception("Заметка не найдена!")
class CommentNotFoundException(commentId: Int, noteId: Int) : Exception("Комментарий не найден!")
class DeleteCommentNotFoundException(commentId: Int) : Exception("Удалённый комментарий не найден!")

object NotesService {
    val databaseNotes: MutableList<Notes> = mutableListOf()
    val databaseComments: MutableList<Comment> = mutableListOf()
    val databaseDeleteComments: MutableList<Comment> = mutableListOf()

    fun addNote(title: String, text: String, noteId: Int) {
        val newNote = Notes(title, text, noteId)
        databaseNotes.add(newNote)
    }

    fun createComment(noteId: Int, message: String, commentId: Int) {
        val newComment = Comment(message, commentId, noteId)
        databaseComments.add(newComment)
    }

    fun deleteNote(noteId: Int) {
        val noteToDelete = databaseNotes.find { it.noteId == noteId }
        if (noteToDelete != null) {
            databaseNotes.remove(noteToDelete)
            databaseComments.removeAll { it.noteId == noteId }
        } else {
            throw NoteNotFoundException(noteId)
        }
    }

    fun deleteComment(commentId: Int, noteId: Int) {
        val commentToDelete = databaseComments.find { it.commentId == commentId && it.noteId == noteId }
        if (commentToDelete != null) {
            databaseComments.remove(commentToDelete)
            databaseDeleteComments.add(commentToDelete)
        } else {
            throw CommentNotFoundException(noteId, commentId)
        }
    }

    fun editNote(noteId: Int, title: String, text: String) {
        var noteToEdit = databaseNotes.find { it.noteId == noteId }
        if (noteToEdit != null) {
            noteToEdit.title = title
            noteToEdit.text = text
        } else {
            throw NoteNotFoundException(noteId)
        }
    }

    fun editComment(commentId: Int, message: String, noteId: Int) {
        var commentToEdit = databaseComments.find { it.commentId == commentId && it.noteId == noteId }
        if (commentToEdit != null) {
            commentToEdit.message = message
        } else {
            throw CommentNotFoundException(commentId, noteId)
        }
    }

    fun getNotes(): List<Notes> {
        return databaseNotes
    }

    fun getNoteById(noteId: Int): Notes? {
        return databaseNotes.find { it.noteId == noteId }
    }

    fun getCommentsByNoteId(noteId: Int): List<Comment> {
        return databaseComments.filter { it.noteId == noteId }
    }

    fun restoreComment(commentId: Int) {
        val commentToRestore = databaseDeleteComments.find { it.commentId == commentId }
        if (commentToRestore != null) {
            databaseDeleteComments.remove(commentToRestore)
            databaseComments.add(commentToRestore)
        } else {
            throw DeleteCommentNotFoundException(commentId)
        }
    }
}

fun main() {

    NotesService.addNote("Купить машину", "Какую машину лучше купить?", 1)
    NotesService.createComment(1, "Бери мерседес!", 1)
    NotesService.addNote("Тюнинг", "Как из жигулей сделать машину?", 2)
    NotesService.deleteComment(1, 1)
    NotesService.editNote(2, "Продать машину", "Как быстро продать жигули?")
    NotesService.restoreComment(1)
    NotesService.editComment(1, "Говорил же, мерседес бери!", 1)
    val notes = NotesService.getNotes()
    println(notes)
    val noteById = NotesService.getNoteById(1)
    val commentsByNoteId = NotesService.getCommentsByNoteId(1)
    println(commentsByNoteId)
    NotesService.deleteComment(1, 1)
    NotesService.deleteNote(1)
    //NotesService.deleteNote(5)
    //NotesService.deleteComment(5, 6)
    println("$notes всё")

}