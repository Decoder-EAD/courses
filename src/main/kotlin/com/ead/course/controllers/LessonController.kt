package com.ead.course.controllers

import com.ead.course.dtos.LessonDto
import com.ead.course.models.LessonModel
import com.ead.course.service.LessonService
import com.ead.course.service.ModuleService
import com.ead.course.spec.SpecificationTemplate
import com.ead.course.spec.lessonModuleId
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/modules/{moduleId}/lessons"], produces = [MediaType.APPLICATION_JSON_VALUE])
class LessonController(
    private val lessonService: LessonService,
    private val moduleService: ModuleService
) {

    @GetMapping
    fun getAllModuleLessons(
        @PathVariable moduleId: UUID,
        spec: SpecificationTemplate.LessonSpec?,
        @PageableDefault(page = 0, size = 10, sort = ["lessonId"], direction = Sort.Direction.ASC) page: Pageable
    ): ResponseEntity<Page<LessonModel>> =
        ResponseEntity.ok(lessonService.getLessonsByModuleId(lessonModuleId(moduleId).and(spec), page))

    @GetMapping("/{lessonId}")
    fun getLessonById(
        @PathVariable moduleId: UUID,
        @PathVariable lessonId: UUID
    ): ResponseEntity<Any> {
        val lessonModel = lessonService.getLessonByModuleIdAndLessonId(moduleId, lessonId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found.")

        return ResponseEntity.ok(lessonModel)
    }

    @PostMapping
    fun createLesson(
        @PathVariable moduleId: UUID,
        @RequestBody @Validated body: LessonDto
    ): ResponseEntity<Any> {
        val moduleModel = moduleService.getModuleById(moduleId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found.")

        val lessonModel = LessonModel()
        BeanUtils.copyProperties(body, lessonModel)
        lessonModel.module = moduleModel

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel))
    }

    @PutMapping("/{lessonId}")
    fun updateLesson(
        @PathVariable moduleId: UUID,
        @PathVariable lessonId: UUID,
        @RequestBody @Validated body: LessonDto
    ): ResponseEntity<Any> {
        val lessonModel = lessonService.getLessonByModuleIdAndLessonId(moduleId, lessonId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found.")

        lessonModel.title = body.title
        lessonModel.description = body.description
        lessonModel.videoUrl = body.videoUrl

        return ResponseEntity.ok(lessonService.save(lessonModel))
    }

    @DeleteMapping("/{lessonId}")
    fun deleteLesson(
        @PathVariable moduleId: UUID,
        @PathVariable lessonId: UUID
    ): ResponseEntity<Any> {
        val lessonModel = lessonService.getLessonByModuleIdAndLessonId(moduleId, lessonId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found.")

        lessonService.deleteLesson(lessonModel)
        return ResponseEntity.ok("Lesson deleted successfully.")
    }
}