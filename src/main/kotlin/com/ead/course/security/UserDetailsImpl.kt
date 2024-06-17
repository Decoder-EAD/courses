package com.ead.course.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

data class UserDetailsImpl(

    val userId: UUID,
    private val authorities: MutableCollection<GrantedAuthority>

) : UserDetails {

    constructor(userId: UUID, roles: String) : this(
        userId = userId,
        authorities = roles.split(",").stream()
            .map { role -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = this.authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String = ""

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}