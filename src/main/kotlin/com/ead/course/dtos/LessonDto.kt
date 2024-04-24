package com.ead.course.dtos

import jakarta.validation.constraints.NotBlank

data class LessonDto (

    @field:NotBlank
    val title: String = "",

    @field:NotBlank
    val description: String = "",

    @field:NotBlank
    val videoUrl: String = ""
){

}
