package com.ead.course.config

import org.apache.logging.log4j.LogManager

abstract class Log {

    val log = LogManager.getLogger(this::class.java)

}