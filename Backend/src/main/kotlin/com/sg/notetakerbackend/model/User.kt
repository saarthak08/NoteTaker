package com.sg.notetakerbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import javax.persistence.*

@Entity
data class User(
    var username: String,

    @get:JsonIgnore
    @set:JsonProperty
    var password: String
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    var name: String="";

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var notes: MutableList<Note> = mutableListOf<Note>();
}