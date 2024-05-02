package com.ead.course.service.impl

import com.ead.course.clients.UserClient
import com.ead.course.models.CourseModel
import com.ead.course.models.CourseUserModel
import com.ead.course.repository.CourseUserRepository
import com.ead.course.service.CourseUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CourseUserServiceImpl(

    private val courseUserRepository: CourseUserRepository,
    private val userClient: UserClient

) : CourseUserService {

    override fun existsByCourseAndUserId(course: CourseModel, userId: UUID): Boolean =
        courseUserRepository.existsByCourseAndUserId(course, userId)

    override fun save(courseUserModel: CourseUserModel): CourseUserModel = courseUserRepository.save(courseUserModel)

    @Transactional
    override fun saveAndSendUserSubscription(courseUserModel: CourseUserModel): CourseUserModel {
        courseUserRepository.save(courseUserModel)

        userClient.postSubscriptionUserInCourse(courseUserModel.course.courseId!!, courseUserModel.userId)
        return courseUserModel
    }
}