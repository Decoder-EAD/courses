package com.ead.course.spec

import com.ead.course.models.CourseModel
import com.ead.course.models.LessonModel
import com.ead.course.models.ModuleModel
import com.ead.course.models.UserModel
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Root
import net.kaczmarzyk.spring.data.jpa.domain.Equal
import net.kaczmarzyk.spring.data.jpa.domain.Like
import net.kaczmarzyk.spring.data.jpa.web.annotation.And
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.data.jpa.domain.Specification
import java.util.*

class SpecificationTemplate {

    @And(
        Spec(path = "courseLevel", spec = Equal::class),
        Spec(path = "courseStatus", spec = Equal::class),
        Spec(path = "name", spec = Like::class),
    )
    interface CourseSpec : Specification<CourseModel> {}

    @Spec(path = "title", spec = Like::class)
    interface ModuleSpec : Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like::class)
    interface LessonSpec : Specification<LessonModel> {}

    @And(
        Spec(path = "email", spec = Like::class),
        Spec(path = "fullName", spec = Like::class),
        Spec(path = "userStatus", spec = Equal::class),
        Spec(path = "userType", spec = Equal::class),
    )
    interface UserSpec: Specification<UserModel> {}

}

fun moduleCourseId(id: UUID): Specification<ModuleModel> {
    return Specification { root: Root<ModuleModel>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
        query.distinct(true)
        val courseRoot: Root<CourseModel> = query.from(CourseModel::class.java)
        val courseId: Expression<UUID> = courseRoot.get("courseId")
        val modules: Expression<Collection<ModuleModel>> = courseRoot.get("modules")
        cb.and(cb.equal(courseId, id), cb.isMember(root, modules))
    }
}

fun lessonModuleId(id: UUID): Specification<LessonModel> {
    return Specification { root: Root<LessonModel>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
        query.distinct(true)
        val moduleRoot: Root<ModuleModel> = query.from(ModuleModel::class.java)
        val moduleId: Expression<UUID> = moduleRoot.get("moduleId")
        val lessons: Expression<Collection<LessonModel>> = moduleRoot.get("lessons")
        cb.and(cb.equal(moduleId, id), cb.isMember(root, lessons))
    }
}

fun userCourseId(id: UUID): Specification<UserModel> {
    return Specification { root: Root<UserModel>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
        query.distinct(true)
        val courseRoot: Root<CourseModel> = query.from(CourseModel::class.java)
        val courseId: Expression<UUID> = courseRoot.get("courseId")
        val users: Expression<Collection<UserModel>> = courseRoot.get("users")
        cb.and(cb.equal(courseId, id), cb.isMember(root, users))
    }
}

fun courseUserId(id: UUID): Specification<CourseModel> {
    return Specification { root: Root<CourseModel>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
        query.distinct(true)
        val userRoot: Root<UserModel> = query.from(UserModel::class.java)
        val userId: Expression<UUID> = userRoot.get("userId")
        val courses: Expression<Collection<CourseModel>> = userRoot.get("courses")
        cb.and(cb.equal(userId, id), cb.isMember(root, courses))
    }
}
