package com.sg.notetakerbackend.model

import java.io.Serializable

data class JWTRequest(var username: String, var password:String) : Serializable;