package com.sg.notetakerbackend.model

import java.io.Serializable

data class JWTResponse(val token: String, var user: User) : Serializable;