package com.ead.course.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.format.DateTimeFormatter

@Configuration
class DateConfig {

    private final val DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val module = JavaTimeModule()
        module.addSerializer(
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT))
        )
        return ObjectMapper().registerModule(module)
    }

}