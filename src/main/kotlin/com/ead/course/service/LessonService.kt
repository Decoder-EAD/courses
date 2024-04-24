package com.ead.course.service

import com.ead.course.models.LessonModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface LessonService {
    fun getLessonsByModuleId(spec: Specification<LessonModel>, page: Pageable): Page<LessonModel>

    fun getLessonByModuleIdAndLessonId(moduleId: UUID, lessonId: UUID): LessonModel?

    fun save(lessonModel: LessonModel): LessonModel

    fun deleteLesson(lessonModel: LessonModel)

}