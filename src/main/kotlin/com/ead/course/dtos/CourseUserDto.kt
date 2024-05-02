package com.ead.course.dtos

import jakarta.validation.constraints.NotNull
import java.util.*

data class CourseUserDto(

    @NotNull
    val courseId: UUID

){}
