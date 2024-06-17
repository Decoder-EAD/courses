package com.ead.course.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    fun getCurrentUser() : UserDetailsImpl {
        return SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
    }

    fun getAuthenticatedUser() : Authentication {
        return SecurityContextHolder.getContext().authentication
    }
}