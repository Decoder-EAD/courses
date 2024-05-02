package com.ead.course.clients

import com.ead.course.config.Log
import com.ead.course.dtos.CourseUserDto
import com.ead.course.dtos.ResponsePageDto
import com.ead.course.dtos.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.UUID

@Component
class UserClient(

    private val restTemplate: RestTemplate,

    @Value("\${ead.api.url.auth-user}")
    private val requestUrl: String

) {

    companion object : Log()

    fun getAllUsersByCourse(courseId: UUID, pageable: Pageable): Page<UserDto> {
        try {
            val sortParam = pageable.sort.toString().replace(": ", ",")
            val url =
                "$requestUrl/users?courseId=$courseId&page=${pageable.pageNumber}&size=${pageable.pageSize}&sort=$sortParam"
            log.info("Request URL: {}", url)

            val responseType = object : ParameterizedTypeReference<ResponsePageDto<UserDto>>() {}
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType).body as Page<UserDto>
        } catch (ex: HttpStatusCodeException) {
            log.error("Error request '/users' {}", ex.message, ex)
        }
        return Page.empty()
    }

    fun getUserById(userId: UUID): ResponseEntity<UserDto> {
        val url = "$requestUrl/users/$userId"
        log.info("Request URL: {}", url)

        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto::class.java)
    }

    fun postSubscriptionUserInCourse(courseId: UUID, userId: UUID) {
        val url = "$requestUrl/users/$userId/courses/subscription"

        restTemplate.postForObject(url, CourseUserDto(courseId), String::class.java)
    }

}