package com.ead.course.validation

import com.ead.course.dtos.CourseDto
import com.ead.course.security.AuthenticationService
import com.ead.course.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import java.util.*

@Component
class CourseValidator(

    @Qualifier("defaultValidator") private val validator: Validator,
    private val userService: UserService,
    private val authenticationService: AuthenticationService

) : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return false
    }

    override fun validate(target: Any, errors: Errors) {
        val courseDto = target as CourseDto
        validator.validate(courseDto, errors)

        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.userInstructor, errors)
        }
    }

    private fun validateUserInstructor(userInstructor: UUID, errors: Errors) {
        val currentUserId = authenticationService.getCurrentUser().userId

        if (currentUserId == userInstructor) {
            val userResponse = userService.findUserById(userInstructor)

            if (userResponse == null) {
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.")
            } else {
                if (userResponse.userType == "STUDENT") {
                    errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.")
                }
            }
        } else {
            throw AccessDeniedException("Forbidden")
        }
    }
}