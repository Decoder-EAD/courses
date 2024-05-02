package com.ead.course.models

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import java.util.UUID

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "TB_COURSES_USERS")
data class CourseUserModel (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    val course: CourseModel,

    @Column(nullable = false)
    val userId: UUID

){
}