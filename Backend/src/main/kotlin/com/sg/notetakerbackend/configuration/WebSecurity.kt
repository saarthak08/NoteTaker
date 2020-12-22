package com.sg.notetakerbackend.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import org.springframework.security.config.http.SessionCreationPolicy

import org.springframework.security.authentication.AuthenticationManager

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.sg.notetakerbackend.security.JWTRequestFilter

import com.sg.notetakerbackend.service.UserService

import com.sg.notetakerbackend.security.JWTAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import java.lang.Exception


@Configuration
@EnableWebSecurity
class WebSecurity @Autowired constructor(
    private val jwtAuthenticationEntryPoint: JWTAuthenticationEntryPoint,
    private val jwtUserDetailsService: UserService,
    private val jwtRequestFilter: JWTRequestFilter,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : WebSecurityConfigurerAdapter() {


    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(jwtUserDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }


    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }


    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.cors()
            .and()
            .csrf().disable() // dont authenticate this particular request
            .authorizeRequests().antMatchers("/api/user/sign-up/**").permitAll()
            .and()
            .authorizeRequests().antMatchers("/api/user/sign-in").permitAll()
            .anyRequest() // all other requests need to be authenticated
            .authenticated().and().exceptionHandling() // make sure we use stateless session; session won't be used to
            .authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

}
