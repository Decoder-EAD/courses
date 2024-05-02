package com.ead.course.service

import com.ead.course.models.CourseModel
import com.ead.course.models.CourseUserModel
import java.util.*

interface CourseUserService {
    fun existsByCourseAndUserId(course: CourseModel, userId: UUID): Boolean

    fun save(courseUserModel: CourseUserModel): CourseUserModel
    fun saveAndSendUserSubscription(courseUserModel: CourseUserModel): CourseUserModel

}