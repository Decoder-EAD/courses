package com.ead.course.controllers

import com.ead.authuser.enums.UserStatus
import com.ead.course.clients.UserClient
import com.ead.course.dtos.SubscriptionDto
import com.ead.course.dtos.UserDto
import com.ead.course.service.CourseService
import com.ead.course.service.CourseUserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpStatusCodeException
import java.util.UUID

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping(path = ["/courses/{courseId}/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CourseUserController(

    private val userClient: UserClient,
    private val courseService: CourseService,
    private val courseUserService: CourseUserService

) {

    @GetMapping
    fun getAllUsersByCourse(
        @PathVariable courseId: UUID,
        @PageableDefault(page = 0, size = 10, sort = ["userId"], direction = Sort.Direction.ASC) page: Pageable
    ): ResponseEntity<Page<UserDto>> = ResponseEntity.ok(userClient.getAllUsersByCourse(courseId, page))

    @PostMapping("/subscription")
    fun saveSubscriptionUserInCourse(
        @PathVariable courseId: UUID,
        @RequestBody @Validated subscription: SubscriptionDto
    ): ResponseEntity<Any> {

        val courseModel = courseService.getCourseById(courseId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.")

        if (courseUserService.existsByCourseAndUserId(courseModel, subscription.userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed.")
        }

        try {
            val userDto = userClient.getUserById(subscription.userId)
            if (userDto.body?.userStatus == UserStatus.BLOCKED) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.")
            }
        } catch (ex: HttpStatusCodeException) {
            if (ex.statusCode == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(ex.statusCode).body("User not found.")
            }
        }

        courseUserService.saveAndSendUserSubscription(courseModel.convertToCourseUserModel(subscription.userId))
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully!")
    }

}