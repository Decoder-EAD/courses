package com.ead.course.models

import com.ead.course.enums.CourseLevel
import com.ead.course.enums.CourseStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Entity
@Table(name = "TB_COURSES")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CourseModel(

    @Id
    val courseId: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 150)
    var name: String = "",

    @Column(nullable = false, length = 250)
    var description: String = "",

    @Column
    var imageUrl: String? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    val creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    var updateDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var courseStatus: CourseStatus = CourseStatus.IN_PROGRESS,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var courseLevel: CourseLevel = CourseLevel.BEGINNER,

    @Column(nullable = false)
    var userInstructor: UUID,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var modules: Set<ModuleModel> = emptySet(),

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TB_COURSES_USERS",
        joinColumns = [JoinColumn(name = "course_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableSet<UserModel> = mutableSetOf()

)