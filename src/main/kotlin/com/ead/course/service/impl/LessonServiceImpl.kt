package com.ead.course.service.impl

import com.ead.course.models.LessonModel
import com.ead.course.repository.LessonRepository
import com.ead.course.service.LessonService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class LessonServiceImpl(
    private val lessonRepository: LessonRepository
) : LessonService {

    override fun getLessonsByModuleId(spec: Specification<LessonModel>, page: Pageable): Page<LessonModel> =
        lessonRepository.findAll(spec, page)

    override fun getLessonByModuleIdAndLessonId(moduleId: UUID, lessonId: UUID): LessonModel? =
        lessonRepository.findLessonByModuleIdAndLessonId(moduleId, lessonId)

    override fun save(lessonModel: LessonModel): LessonModel = lessonRepository.save(lessonModel)

    override fun deleteLesson(lessonModel: LessonModel) = lessonRepository.delete(lessonModel)
}