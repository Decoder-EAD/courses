package com.ead.course.dtos

import java.util.*

data class UserEventDto(

    var userId: UUID = UUID.randomUUID(),
    var userName: String = "",
    var email: String = "",
    var fullName: String = "",
    var userStatus: String = "",
    var userType: String = "",
    var cpf: String? = null,
    var imageUrl: String? = null,
    var actionType: String = ""

)