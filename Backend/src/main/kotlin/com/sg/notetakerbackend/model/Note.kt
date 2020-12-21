package com.sg.notetakerbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class Note(
    var title: String,
    var description: String = "",

    @get:JsonIgnore
    @set:JsonProperty
    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    var user: User
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0;
}