package com.ead.course.models

import com.ead.course.dtos.UserEventDto
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.util.*

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "TB_USERS")
data class UserModel (

    @Id
    val userId: UUID,

    @Column(nullable = false, unique = true, length = 50)
    val email: String,

    @Column(nullable = false, length = 150)
    val fullName: String,

    @Column(nullable = false)
    val userStatus: String,

    @Column(nullable = false)
    val userType: String,

    @Column(length = 20)
    val cpf: String?,

    @Column
    val imageUrl: String?,

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val courses: Set<CourseModel> = emptySet()

) {

    constructor(source: UserEventDto): this(
        source.userId,
        source.email,
        source.fullName,
        source.userStatus,
        source.userType,
        source.cpf,
        source.imageUrl
    )

}