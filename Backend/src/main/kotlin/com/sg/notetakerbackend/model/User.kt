package com.sg.notetakerbackend.model

import javax.persistence.*

@Entity
data class User(var name:String, var email:String) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0;

    @Transient
    var password:String="";

    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    var notes: List<Note> = emptyList();

}