package com.sg.notetakerbackend.repository

import com.sg.notetakerbackend.model.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository: JpaRepository<Note,Long> {

}