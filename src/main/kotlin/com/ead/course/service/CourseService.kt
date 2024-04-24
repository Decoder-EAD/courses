package com.ead.course.service

import com.ead.course.models.CourseModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface CourseService {

    fun getCourses(spec: Specification<CourseModel>?, page: Pageable): Page<CourseModel>

    fun getCourseById(courseId: UUID): CourseModel?

    fun save(courseModel: CourseModel): CourseModel

    fun delete(courseModel: CourseModel)

}