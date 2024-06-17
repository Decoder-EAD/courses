package com.ead.course.service.impl

import com.ead.course.config.Log
import com.ead.course.dtos.NotificationCommandDto
import com.ead.course.models.CourseModel
import com.ead.course.models.UserModel
import com.ead.course.publisher.NotificationCommandPublisher
import com.ead.course.repository.CourseRepository
import com.ead.course.repository.LessonRepository
import com.ead.course.repository.ModuleRepository
import com.ead.course.service.CourseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CourseServiceImpl(

    private val courseRepository: CourseRepository,
    private val moduleRepository: ModuleRepository,
    private val lessonRepository: LessonRepository,
    private val notificationCommandPublisher: NotificationCommandPublisher

) : CourseService {

    companion object: Log()

    override fun getCourses(spec: Specification<CourseModel>?, page: Pageable): Page<CourseModel> =
        if (spec != null) courseRepository.findAll(spec, page) else courseRepository.findAll(page)

    override fun getCourseById(courseId: UUID): CourseModel? = courseRepository.findByIdOrNull(courseId)

    override fun save(courseModel: CourseModel): CourseModel = courseRepository.save(courseModel)

    @Transactional
    override fun delete(courseModel: CourseModel) {
        val modules = moduleRepository.findAllModulesByCourseId(courseModel.courseId)
        if (modules.isNotEmpty()) {
            modules.forEach {
                val lessons = lessonRepository.findLessonByModuleId(it.moduleId!!)
                lessonRepository.deleteAll(lessons)
            }
        }

        courseRepository.deleteCourseUserByCourseId(courseModel.courseId)
        courseRepository.delete(courseModel)
    }

    override fun existsByCourseAndUser(courseId: UUID, userId: UUID): Boolean {
        return courseRepository.existsByCourseAnUser(courseId, userId)
    }

    @Transactional
    override fun saveSubscriptionUserInCourse(courseId: UUID, userId: UUID) {
        courseRepository.saveCourseUser(courseId, userId)
    }

    @Transactional
    override fun saveSubscriptionUserInCourseSendNotificationSubscription(course: CourseModel, user: UserModel) {
        courseRepository.saveCourseUser(course.courseId, user.userId)
        try {
            val notification = NotificationCommandDto(
                "Bem-vindo(a) ao curso ${course.name}",
                "${user.fullName} a sua inscricão foi realizado com sucesso!",
                user.userId
            )

            notificationCommandPublisher.publishNotificationCommand(notification)
        } catch (e: Exception) {
            log.warn("Error sending notification", e)
        }
    }
}