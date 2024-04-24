package com.ead.course.dtos

import com.ead.course.enums.CourseLevel
import com.ead.course.enums.CourseStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class CourseDto(

    @field:NotBlank
    var name: String = "",

    @field:NotBlank
    var description : String = "",

    var imageUrl: String? = null,

    @field:NotNull
    var courseStatus: CourseStatus = CourseStatus.IN_PROGRESS,

    @field:NotNull
    var courseLevel: CourseLevel = CourseLevel.BEGINNER,

    @field:NotNull
    var userInstructor: UUID

) {
}