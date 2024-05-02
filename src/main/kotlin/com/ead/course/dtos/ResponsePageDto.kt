package com.ead.course.dtos

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponsePageDto<T>
@JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @JsonProperty("content") private val content: List<T>,
    @JsonProperty("number") private val number: Int,
    @JsonProperty("size") private val size: Int,
    @JsonProperty("totalElements") private val totalElements: Long,
    @JsonProperty("pageable") private val pageable: JsonNode,
    @JsonProperty("last") private val last: Boolean,
    @JsonProperty("totalPages") private val totalPages: Int,
    @JsonProperty("sort") private val sort: JsonNode,
    @JsonProperty("first") private val first: Boolean,
    @JsonProperty("empty") private val empty: Boolean,
) : PageImpl<T>(content, PageRequest.of(number, size), totalElements) {

}