package com.sg.notetakerbackend.repository

import com.sg.notetakerbackend.model.Note
import com.sg.notetakerbackend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository: JpaRepository<Note,Long> {

    fun deleteNoteByUser(user: User);

    fun deleteAllByUser(user:User);

}