package com.ead.course.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Entity
@Table(name = "TB_LESSONS")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class LessonModel(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var lessonId: UUID? = null,

    @Column(nullable = false, length = 150)
    var title: String = "",

    @Column(nullable = false, length = 250)
    var description: String = "",

    @Column(nullable = false)
    var videoUrl: String = "",

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    var creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    var module: ModuleModel? = null

) {

}