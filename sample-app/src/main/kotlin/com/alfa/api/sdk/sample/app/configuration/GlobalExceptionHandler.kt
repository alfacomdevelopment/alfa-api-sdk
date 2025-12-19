package com.alfa.api.sdk.sample.app.configuration

import com.alfa.api.sdk.client.exceptions.ApiException
import com.alfa.api.sdk.common.exceptions.SdkException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    data class ErrorResponse(
        val status: Int,
        val message: String,
        val details: String? = null
    )

    @ExceptionHandler(SdkException::class)
    fun handleSdkException(e: SdkException): ResponseEntity<ErrorResponse> {
        val cause = e.cause
        if (cause is ApiException) {
            val bodyAsString = try {
                String(cause.response ?: ByteArray(0))
            } catch (_: Exception) {
                null
            }

            log.error(
                "Got an SDK Error with ApiException. Code: {}, response: {}",
                cause.statusCode,
                bodyAsString,
                e
            )

            return ResponseEntity
                .status(HttpStatus.valueOf(cause.statusCode))
                .body(
                    ErrorResponse(
                        status = cause.statusCode,
                        message = e.message ?: "SDK error",
                        details = bodyAsString
                    )
                )
        }

        log.error("Got SDK error", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = e.message ?: "SDK error"
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleAnyException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("Got an error", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = e.message ?: "Unexpected error"
                )
            )
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}

