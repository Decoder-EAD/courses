package com.ead.course.dtos

import java.util.*

data class NotificationCommandDto(

    val title: String,
    val message: String,
    val userId: UUID

)