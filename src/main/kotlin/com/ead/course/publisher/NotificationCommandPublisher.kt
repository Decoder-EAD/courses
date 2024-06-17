package com.ead.course.publisher

import com.ead.course.dtos.NotificationCommandDto
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotificationCommandPublisher(

    private val rabbitTemplate: RabbitTemplate,

    @Value("\${ead.broker.exchange.notification-command-exchange}")
    private val notificationCommandExchange: String,

    @Value("\${ead.broker.key.notification-command-key}")
    private val notificationCommandKey: String

) {

    fun publishNotificationCommand(notificationCommandDto: NotificationCommandDto) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDto)
    }

}