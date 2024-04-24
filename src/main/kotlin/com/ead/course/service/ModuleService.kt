package com.ead.course.service

import com.ead.course.models.ModuleModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.*

interface ModuleService {

    fun getModuleById(moduleId: UUID): ModuleModel?

    fun getModulesByCourseId(spec: Specification<ModuleModel>, page: Pageable): Page<ModuleModel>

    fun getModuleByCourseIdAndModuleId(courseId: UUID, moduleId: UUID): ModuleModel?

    fun save(moduleModel: ModuleModel): ModuleModel

    fun delete(moduleModel: ModuleModel)

}