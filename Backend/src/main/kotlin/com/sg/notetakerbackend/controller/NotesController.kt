package com.sg.notetakerbackend.controller

import com.sg.notetakerbackend.model.Note
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notes")
class NotesController {

    @GetMapping()
    fun getNotes():List<Note> {
        return listOf(Note("Hello 1").apply { description="description 1" },Note("Hello 2"));
    }

}