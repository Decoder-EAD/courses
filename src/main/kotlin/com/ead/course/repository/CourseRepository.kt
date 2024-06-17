package com.ead.course.repository

import com.ead.course.models.CourseModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CourseRepository : JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(
        "SELECT CASE WHEN COUNT(tcu) > 0 THEN true ELSE false END FROM tb_courses_users tcu WHERE tcu.course_id = :courseId AND tcu.user_id = :userId",
        nativeQuery = true
    )
    fun existsByCourseAnUser(@Param("courseId") courseId: UUID, @Param("userId") userId: UUID): Boolean

    @Modifying
    @Query("INSERT INTO tb_courses_users values(:courseId, :userId)", nativeQuery = true)
    fun saveCourseUser(@Param("courseId") courseId: UUID, @Param("userId") userId: UUID)

    @Modifying
    @Query("DELETE FROM tb_courses_users WHERE course_id = :courseId", nativeQuery = true)
    fun deleteCourseUserByCourseId(@Param("courseId") courseId: UUID)

    @Modifying
    @Query("DELETE FROM tb_courses_users WHERE userId = :userId", nativeQuery = true)
    fun deleteCourseUserByUserId(@Param("userId") userId: UUID)

}