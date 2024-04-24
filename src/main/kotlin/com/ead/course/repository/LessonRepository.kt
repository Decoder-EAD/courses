package com.ead.course.repository

import com.ead.course.models.LessonModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LessonRepository : JpaRepository<LessonModel, UUID>, JpaSpecificationExecutor<LessonModel> {

    @Query(value = "SELECT lm FROM LessonModel lm WHERE lm.module.moduleId = :moduleId AND lm.id = :lessonId")
    fun findLessonByModuleIdAndLessonId(moduleId: UUID, lessonId: UUID): LessonModel?

}