package com.sg.notetakerbackend.controller

import com.sg.notetakerbackend.model.JWTRequest
import com.sg.notetakerbackend.model.JWTResponse
import com.sg.notetakerbackend.model.User
import com.sg.notetakerbackend.security.JWTTokenUtil
import com.sg.notetakerbackend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.security.core.userdetails.UserDetails

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.authentication.AuthenticationManager

import javax.servlet.http.HttpServletRequest

import javax.servlet.http.HttpServletResponse

import org.springframework.web.bind.annotation.RequestMethod

import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.security.authentication.BadCredentialsException

import org.springframework.security.authentication.DisabledException

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import org.springframework.web.server.ResponseStatusException

import org.springframework.web.bind.annotation.RequestBody
import java.lang.IllegalArgumentException
import javax.security.sasl.AuthenticationException


@RestController
@RequestMapping("/api/user")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtTokenUtil: JWTTokenUtil,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody user: User): Any {
        try {
            user.password = bCryptPasswordEncoder.encode(user.password);
            userService.addUser(user);
        } catch (e: Exception) {
            return if (e.message == "User Already Present") {
                ResponseEntity("Username Already Present", HttpStatus.CONFLICT);
            } else {
                ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return ResponseEntity("User with " + user.username + " created!", HttpStatus.OK);
    }

    @RequestMapping(value = ["/sign-in"], method = [RequestMethod.POST])
    @Throws(java.lang.Exception::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JWTRequest): ResponseEntity<*>? {
        authenticate(authenticationRequest.username, authenticationRequest.password)
        val userDetails: UserDetails = userService
            .loadUserByUsername(authenticationRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok<Any>(token!!);
    }


    @Throws(java.lang.Exception::class)
    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw java.lang.Exception("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw java.lang.Exception("INVALID_CREDENTIALS", e)
        }
    }

    @RequestMapping(value = ["/is-logged-in"], method = [RequestMethod.POST])
    fun authenticateToken(response: HttpServletResponse?, request: HttpServletRequest): ResponseEntity<*>? {
        val requestTokenHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7)
            username = try {
                jwtTokenUtil.getUsernameFromToken(jwtToken)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity("Unable to get JWT Token", HttpStatus.UNAUTHORIZED)
            } catch (e: ExpiredJwtException) {
                return ResponseEntity("JWT Token has expired", HttpStatus.UNAUTHORIZED)
            }
        } else {
            return ResponseEntity("JWT Token does not begin with Bearer String", HttpStatus.UNAUTHORIZED)
        }
        if (username != null) {
            val userDetails: UserDetails = this.userService.loadUserByUsername(username)
            return if (jwtTokenUtil.validateToken(jwtToken, userDetails)!!) {
                ResponseEntity.ok().build<Any>()
            } else ResponseEntity.status(401).build<Any>()
        }
        return ResponseEntity.ok().build<Any>()
    }
}