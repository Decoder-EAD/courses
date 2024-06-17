package com.ead.course.controllers

import com.ead.course.dtos.CourseDto
import com.ead.course.models.CourseModel
import com.ead.course.service.CourseService
import com.ead.course.spec.SpecificationTemplate
import com.ead.course.spec.courseUserId
import com.ead.course.validation.CourseValidator
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/courses", produces = [MediaType.APPLICATION_JSON_VALUE])
class CourseController(

    private val courseService: CourseService,
    private val courseValidator: CourseValidator

) {

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    fun getCourses(
        spec: SpecificationTemplate.CourseSpec?,
        @PageableDefault(page = 0, size = 10, sort = ["courseId"], direction = Sort.Direction.ASC) page: Pageable,
        @RequestParam(required = false) userId: UUID?
    ): ResponseEntity<Page<CourseModel>> {
        val coursePage = if (userId != null) {
            courseService.getCourses(courseUserId(userId).and(spec), page)
        } else {
            courseService.getCourses(spec, page)
        }

        return ResponseEntity.ok(coursePage)
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("{courseId}")
    fun getCourseById(@PathVariable courseId: UUID): ResponseEntity<Any> {
        return ResponseEntity.ok(courseService.getCourseById(courseId))
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    fun createCourse(@RequestBody requestBody: CourseDto, errors: Errors): ResponseEntity<Any> {
        courseValidator.validate(requestBody, errors)
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.allErrors)
        }

        val courseModel = CourseModel(userInstructor = requestBody.userInstructor)
        BeanUtils.copyProperties(requestBody, courseModel)
        courseService.save(courseModel)
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel)
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/{courseId}")
    fun deleteCourse(@PathVariable courseId: UUID): ResponseEntity<Any> {
        val courseModel = courseService.getCourseById(courseId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.")

        courseService.delete(courseModel)
        return ResponseEntity.ok("Course deleted successfully.")
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: UUID,
        @RequestBody @Validated requestBody: CourseDto
    ): ResponseEntity<Any> {
        val courseModel = courseService.getCourseById(courseId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.")

        courseModel.name = requestBody.name
        courseModel.description = requestBody.description
        courseModel.imageUrl = requestBody.imageUrl
        courseModel.courseStatus = requestBody.courseStatus
        courseModel.courseLevel = requestBody.courseLevel
        courseModel.updateDate = LocalDateTime.now(ZoneId.of("UTC"))

        courseService.save(courseModel)
        return ResponseEntity.ok(courseModel)
    }

}
