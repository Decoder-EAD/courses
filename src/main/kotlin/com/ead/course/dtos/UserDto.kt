package com.ead.course.dtos

import com.ead.authuser.enums.UserStatus
import com.ead.authuser.enums.UserType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.UUID

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(

    val userId: UUID? = null,
    val userName: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val userStatus: UserStatus = UserStatus.ACTIVE,
    val userType: UserType = UserType.STUDENT,
    val cpf: String = "",
    val imageUrl: String = ""

) {

}
