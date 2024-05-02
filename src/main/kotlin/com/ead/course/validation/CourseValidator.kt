package com.ead.course.validation

import com.ead.authuser.enums.UserType
import com.ead.course.clients.UserClient
import com.ead.course.dtos.CourseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.client.HttpStatusCodeException
import java.util.UUID

@Component
class CourseValidator(

    @Qualifier("defaultValidator")
    private val validator: Validator,
    private val userClient: UserClient

): Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return false
    }

    override fun validate(target: Any, errors: Errors) {
        val courseDto = target as CourseDto
        validator.validate(courseDto, errors)

        if(!errors.hasErrors()) {
            validateUserInstructor(courseDto.userInstructor, errors)
        }
    }

    private fun validateUserInstructor(userInstructor: UUID, errors: Errors) {
        try {
            val userResponse = userClient.getUserById(userInstructor)
            if (userResponse.body?.userType == UserType.STUDENT) {
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.")
            }
        } catch (e: HttpStatusCodeException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) {
                errors.rejectValue("userInstructor", "UserInstructorError", "User not found.")
            }
        }
    }
}