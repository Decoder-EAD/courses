package com.ead.course.repository

import com.ead.course.models.CourseModel
import com.ead.course.models.CourseUserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CourseUserRepository: JpaRepository<CourseUserModel, UUID> {

    fun existsByCourseAndUserId(course: CourseModel, userId: UUID): Boolean

}