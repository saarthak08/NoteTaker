package com.sg.notetakerbackend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
data class Note(
    var title: String,
    var description: String,
):Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @get:net.minidev.json.annotate.JsonIgnore
    @set:JsonProperty
    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    var user: User?=null;
}