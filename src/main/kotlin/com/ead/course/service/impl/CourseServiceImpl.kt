package com.ead.course.service.impl

import com.ead.course.models.CourseModel
import com.ead.course.repository.CourseRepository
import com.ead.course.service.CourseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseServiceImpl(
    private val courseRepository: CourseRepository
) : CourseService {

    override fun getCourses(spec: Specification<CourseModel>?, page: Pageable): Page<CourseModel> =
        if (spec != null) courseRepository.findAll(spec, page) else courseRepository.findAll(page)

    override fun getCourseById(courseId: UUID): CourseModel? = courseRepository.findByIdOrNull(courseId)

    override fun save(courseModel: CourseModel): CourseModel = courseRepository.save(courseModel)

    override fun delete(courseModel: CourseModel) = courseRepository.delete(courseModel)
}