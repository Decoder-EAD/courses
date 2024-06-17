package com.ead.course.controllers

import com.ead.authuser.enums.UserStatus
import com.ead.course.dtos.SubscriptionDto
import com.ead.course.service.CourseService
import com.ead.course.service.UserService
import com.ead.course.spec.SpecificationTemplate
import com.ead.course.spec.userCourseId
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
@RequestMapping(path = ["/courses/{courseId}/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CourseUserController(

    private val courseService: CourseService,
    private val userService: UserService

) {

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping
    fun getAllUsersByCourse(
        spec: SpecificationTemplate.UserSpec,
        @PathVariable courseId: UUID,
        @PageableDefault(page = 0, size = 10, sort = ["userId"], direction = Sort.Direction.ASC) page: Pageable
    ): ResponseEntity<Any> = ResponseEntity.ok(userService.findAll(userCourseId(courseId).and(spec), page))

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/subscription")
    fun saveSubscriptionUserInCourse(
        @PathVariable courseId: UUID,
        @RequestBody @Validated subscription: SubscriptionDto
    ): ResponseEntity<Any> {

        val courseModel = courseService.getCourseById(courseId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.")
        val userModel = userService.findUserById(subscription.userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.")

        if (courseService.existsByCourseAndUser(courseId, subscription.userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Subscription already exists.")
        }

        if (userModel.userStatus == UserStatus.BLOCKED.toString()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.")
        }

        courseService.saveSubscriptionUserInCourseSendNotificationSubscription(courseModel, userModel)

        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully!")
    }

}