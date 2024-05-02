package com.ead.course.dtos

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class SubscriptionDto(

    @NotNull
    val userId: UUID

) {}
