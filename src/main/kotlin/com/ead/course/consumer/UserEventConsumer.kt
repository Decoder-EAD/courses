package com.ead.course.consumer

import com.ead.course.dtos.UserEventDto
import com.ead.course.enums.ActionType
import com.ead.course.models.UserModel
import com.ead.course.service.UserService
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class UserEventConsumer(

    private val userService: UserService

) {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(value = "\${ead.broker.queue.user-event-queue.name}", durable = "true"),
                exchange = Exchange(value = "\${ead.broker.exchange.user-event}", type = ExchangeTypes.FANOUT)
            )
        ]
    )
    fun listenUserEvent(@Payload payload: UserEventDto) {
        val userModel = UserModel(payload)

        when (ActionType.valueOf(payload.actionType)) {
            ActionType.CREATE, ActionType.UPDATE -> userService.save(userModel)
            ActionType.DELETE -> userService.delete(payload.userId)
        }
    }

}