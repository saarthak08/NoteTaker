package com.sg.notetakerbackend.service

import com.sg.notetakerbackend.model.Note
import com.sg.notetakerbackend.model.User
import com.sg.notetakerbackend.repository.NoteRepository
import com.sg.notetakerbackend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class NoteService @Autowired constructor(private val noteRepository: NoteRepository,private val userRepository: UserRepository) {

    @Throws(Exception::class)
    fun addNote(note:Note,user:User) {
        note.user = user;
        user.notes.add(note);
        noteRepository.save(note);
        userRepository.save(user);
    }

    @Throws(Exception::class)
    fun deleteNote(noteId:Long,user:User) {
        user.notes.removeIf {
            it.id==noteId;
        }
        userRepository.save(user);
        noteRepository.deleteById(noteId);
    }

    fun updateNote(note:Note,user:User) {
        val index=user.notes.find {
            it.id==note.id
        }?.id;
        if (index != null) {
            user.notes[index.toInt()] = note
        };
        noteRepository.save(note);
        userRepository.save(user);
    }

    fun getAllNotes(user:User):MutableList<Note> {
        return user.notes;
    }
}