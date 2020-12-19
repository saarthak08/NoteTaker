package com.sg.notetakerbackend.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Note(var title: String) {

    @Id
    var id:Long=0;

    var description:String="";

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name="user_id")
    var user:User?=null;
}