package com.ead.course.repository

import com.ead.course.models.ModuleModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ModuleRepository : JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

    @Query(value = "SELECT mm FROM ModuleModel mm WHERE mm.course.courseId = :courseId")
    fun findAllModulesByCourseId(courseId: UUID): List<ModuleModel>

    @Query(value = "SELECT mm FROM ModuleModel mm WHERE mm.course.courseId = :courseId AND mm.id = :moduleId")
    fun findAllModulesByCourseIdAndModuleId(courseId: UUID, moduleId: UUID): ModuleModel?

}