package com.sg.notetakerbackend.service

import com.sg.notetakerbackend.model.Note
import com.sg.notetakerbackend.model.User
import com.sg.notetakerbackend.repository.NoteRepository
import com.sg.notetakerbackend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class NoteService @Autowired constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) {

    @Throws(Exception::class)
    fun addNote(note: Note, user: User) {
        note.user = user;
        user.notes.add(note);
        noteRepository.save(note);
        userRepository.save(user);
    }

    @Throws(Exception::class)
    fun deleteNote(note: Note, user: User) {
        val dbNote: Note = noteRepository.findByIdOrNull(note.id) ?: throw Exception("No note found with id=${note.id} for current user!");
        user.notes.removeIf {
            it.id == note.id;
        }
        userRepository.save(user);
        noteRepository.delete(dbNote);
    }

    @Throws(Exception::class)
    fun updateNote(note: Note, user: User) {
        val dbNote: Note = noteRepository.findByIdOrNull(note.id) ?: throw Exception("No note found with id=${note.id} for current user!");
        dbNote.title=note.title;
        dbNote.description=note.description;
        user.notes.forEach {
            if (it.id == note.id) {
                it.title = note.title;
                it.description = note.description;
            }
        };
        noteRepository.save(dbNote);
        userRepository.save(user);
    }

    fun getAllNotes(user: User): MutableList<Note> {
        return user.notes;
    }
}