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
class UserService @Autowired constructor(private val userRepository: UserRepository):UserDetailsService {


    companion object {
        private var user:User?=null;

        @Autowired
        private var userRepository:UserRepository?=null;

        @Throws(AuthenticationException::class)
        fun getCurrentUser(): User {
            return if(user==null) {
                if (SecurityContextHolder.getContext().authentication.isAuthenticated) {
                    val authContext = SecurityContextHolder.getContext().authentication;
                    val user:User= userRepository!!.findByUsername(authContext.name);
                    user;
                } else {
                    throw AuthenticationException("User Not Authenticated");
                }
            } else {
                user as User;
            }
        }
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        var user: User? = userRepository.findByUsername(username!!);
        user ?: throw UsernameNotFoundException(username);
        return org.springframework.security.core.userdetails.User(user.username, user.password, emptyList());
    }

    fun addUser(user:User) {
        userRepository.save(user);
    }

}