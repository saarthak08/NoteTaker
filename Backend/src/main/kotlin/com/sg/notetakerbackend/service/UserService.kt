package com.sg.notetakerbackend.service

import com.sg.notetakerbackend.model.Note
import com.sg.notetakerbackend.model.User
import com.sg.notetakerbackend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.security.sasl.AuthenticationException
import kotlin.jvm.Throws

@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) : UserDetailsService {

    @kotlin.jvm.Throws(AuthenticationException::class)
    fun getCurrentUser(): User? {
        return if (SecurityContextHolder.getContext().authentication.isAuthenticated) {
            val authContext = SecurityContextHolder.getContext().authentication;
            val user: User? = findUserByUsername(authContext.name);
            user;
        } else {
            throw AuthenticationException("User Not Authenticated");
        }

    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        var user: User? = userRepository.findByUsername(username!!);
        user ?: throw UsernameNotFoundException(username);
        return org.springframework.security.core.userdetails.User(user.username, user.password, emptyList());
    }

    @Throws(Exception::class)
    fun addUser(user: User) {
        var userExists: User? = userRepository.findByUsername(user.username);
        if (userExists != null) {
            throw Exception("User Already Present");
        }
        userRepository.save(user);
    }

    fun findUserByUsername(username: String): User? {
        return userRepository.findByUsername(username);
    }

}