package com.sg.notetakerbackend.model

import com.fasterxml.jackson.annotation.JsonProperty
import net.minidev.json.annotate.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import javax.persistence.*

@Entity
data class User(var name:String, var username:String):Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0;

    @get:JsonIgnore
    @set:JsonProperty
    @Transient
    var password:String="";


    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var notes: MutableList<Note> = mutableListOf<Note>();
}