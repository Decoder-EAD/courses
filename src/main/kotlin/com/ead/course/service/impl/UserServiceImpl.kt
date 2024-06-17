package com.ead.course.service.impl

import com.ead.course.models.UserModel
import com.ead.course.repository.CourseRepository
import com.ead.course.repository.UserRepository
import com.ead.course.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserServiceImpl(

    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository

) : UserService {

    override fun findAll(spec: Specification<UserModel>?, page: Pageable): Page<UserModel> =
        if (spec != null) userRepository.findAll(spec, page) else userRepository.findAll(page)

    override fun findUserById(userId: UUID): UserModel? = userRepository.findByIdOrNull(userId)

    override fun save(userModel: UserModel): UserModel = userRepository.save(userModel)

    @Transactional
    override fun delete(userId: UUID) {
        courseRepository.deleteCourseUserByUserId(userId)
        userRepository.deleteById(userId)
    }
}