package com.ead.course.controllers

import com.ead.course.dtos.ModuleDto
import com.ead.course.models.ModuleModel
import com.ead.course.service.CourseService
import com.ead.course.service.ModuleService
import com.ead.course.spec.SpecificationTemplate
import com.ead.course.spec.moduleCourseId
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/courses/{courseId}/modules"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ModuleController(
    private val moduleService: ModuleService,
    private val courseService: CourseService
) {

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    fun getAllCourseModules(
        @PathVariable courseId: UUID,
        spec: SpecificationTemplate.ModuleSpec?,
        @PageableDefault(page = 0, size = 10, sort = ["moduleId"], direction = Sort.Direction.ASC) page: Pageable
    ): ResponseEntity<Page<ModuleModel>> =
        ResponseEntity.ok(moduleService.getModulesByCourseId(moduleCourseId(courseId).and(spec), page))

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/{moduleId}")
    fun getModuleById(
        @PathVariable courseId: UUID,
        @PathVariable moduleId: UUID
    ): ResponseEntity<Any> {
        val moduleModel = moduleService.getModuleByCourseIdAndModuleId(courseId, moduleId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.")

        return ResponseEntity.ok(moduleModel)
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    fun createModule(
        @PathVariable courseId: UUID,
        @RequestBody @Validated moduleDto: ModuleDto
    ): ResponseEntity<Any> {
        val courseModel = courseService.getCourseById(courseId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.")

        val moduleModel = ModuleModel()
        BeanUtils.copyProperties(moduleDto, moduleModel)
        moduleModel.course = courseModel

        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel))
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{moduleId}")
    fun deleteModule(
        @PathVariable courseId: UUID,
        @PathVariable moduleId: UUID
    ): ResponseEntity<Any> {
        val moduleModel = moduleService.getModuleByCourseIdAndModuleId(courseId, moduleId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.")

        moduleService.delete(moduleModel)
        return ResponseEntity.ok("Module deleted successfully.")
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{moduleId}")
    fun updateModule(
        @PathVariable courseId: UUID,
        @PathVariable moduleId: UUID,
        @RequestBody @Validated moduleDto: ModuleDto
    ): ResponseEntity<Any> {
        val moduleModel = moduleService.getModuleByCourseIdAndModuleId(courseId, moduleId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.")

        moduleModel.title = moduleDto.title
        moduleModel.description = moduleDto.description

        moduleService.save(moduleModel)
        return ResponseEntity.ok(moduleModel)
    }

}



