package com.alfa.api.sdk.sample.app.configuration

import com.alfa.api.sdk.client.exceptions.ApiException
import com.alfa.api.sdk.common.exceptions.SdkException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleAnyException(e: Exception): String {
        if (e is SdkException && e.cause != null && e.cause is ApiException) {
            val apiException = e.cause as ApiException
            log.error(
                "Got an SDK Error with ApiException. Code: {}, response: {}",
                apiException.statusCode, String(apiException.response), e
            )
        } else {
            log.error("Got an error", e)
        }
        return "Got an error: " + e.message
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}

