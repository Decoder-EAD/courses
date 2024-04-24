package com.ead.course.service.impl

import com.ead.course.models.ModuleModel
import com.ead.course.repository.ModuleRepository
import com.ead.course.service.ModuleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ModuleServiceImpl(
    private val moduleRepository: ModuleRepository
) : ModuleService {

    override fun getModuleById(moduleId: UUID): ModuleModel? = moduleRepository.findByIdOrNull(moduleId)

    override fun getModulesByCourseId(spec: Specification<ModuleModel>, page: Pageable): Page<ModuleModel> =
        moduleRepository.findAll(spec, page)

    override fun getModuleByCourseIdAndModuleId(courseId: UUID, moduleId: UUID): ModuleModel? =
        moduleRepository.findAllModulesByCourseIdAndModuleId(courseId, moduleId)

    override fun save(moduleModel: ModuleModel): ModuleModel = moduleRepository.save(moduleModel)

    override fun delete(moduleModel: ModuleModel) = moduleRepository.delete(moduleModel)

}