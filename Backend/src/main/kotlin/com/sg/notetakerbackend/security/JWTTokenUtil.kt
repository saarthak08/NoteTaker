package com.sg.notetakerbackend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable

import org.springframework.security.core.userdetails.UserDetails

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import java.util.function.Function;



@Component
class JWTTokenUtil :Serializable {
    val JWT_TOKEN_VALIDITY:Long = (20 * 60 * 60);

    @Value("\${jwt.secret}")
    private val secret: String? = null


    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token, Claims::getSubject)
    }


    fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken<Date>(token, Claims::getExpiration)
    }



    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims,T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }


    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody()
    }


    private fun isTokenExpired(token: String?): Boolean? {
        val expiration: Date = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }


    fun generateToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, userDetails.username)
    }


    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String? {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, secret).compact()
    }


    fun validateToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)!!
    }


}