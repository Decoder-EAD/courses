package com.ead.course.service

import com.ead.course.models.UserModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface UserService {

    fun findAll(spec: Specification<UserModel>?, page: Pageable): Page<UserModel>
    fun findUserById(userId: UUID): UserModel?

    fun save(userModel: UserModel): UserModel
    fun delete(userId: UUID)

}