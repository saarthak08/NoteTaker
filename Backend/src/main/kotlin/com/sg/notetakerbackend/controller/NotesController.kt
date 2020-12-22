package com.sg.notetakerbackend.controller

import com.sg.notetakerbackend.model.Note
import com.sg.notetakerbackend.model.User
import com.sg.notetakerbackend.service.NoteService
import com.sg.notetakerbackend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/notes")
class NotesController @Autowired constructor(
    private val noteService: NoteService,
    private val userService: UserService
) {


    @PostMapping()
    fun addNote(@RequestBody note: Note): Any {
        noteService.addNote(note, userService.getCurrentUser()!!);
        return ResponseEntity("",HttpStatus.OK);
    }

    @DeleteMapping
    fun deleteNote(@RequestBody id: Long): Any {
        noteService.deleteNote(id, userService.getCurrentUser()!!);
        return ResponseEntity("",HttpStatus.OK);
    }

    @PutMapping
    fun updateNote(@RequestBody note: Note): Any {
        noteService.updateNote(note, userService.getCurrentUser()!!);
        return ResponseEntity("",HttpStatus.OK);
    }

    @GetMapping
    fun getAllNotes(): Any {
        return noteService.getAllNotes(userService.getCurrentUser()!!);
    }
}