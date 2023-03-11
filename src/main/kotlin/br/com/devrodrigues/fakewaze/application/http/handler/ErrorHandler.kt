package br.com.devrodrigues.fakewaze.application.http.handler

import br.com.devrodrigues.fakewaze.application.http.handler.dto.ErrorResponse
import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

suspend fun Throwable.toServerResponse(): ServerResponse {

    val (statusCode, response) = toResponse()

    return ServerResponse.status(statusCode).bodyValueAndAwait(response)
}

private fun Throwable.toResponse(): Pair<HttpStatus, ErrorResponse> =
    when (this) {
        is WazeException -> when (this) {
            is WazeException.WazeNotFoundException -> HttpStatus.NOT_FOUND to ErrorResponse(
                message = "Waze not found",
            )

            is WazeException.WazeClassPathResourceNotFoundException -> HttpStatus.NOT_FOUND to ErrorResponse(
                message = "Waze class path resource not found",
            )
        }

        else -> {
            HttpStatus.INTERNAL_SERVER_ERROR to ErrorResponse(
                message = "Internal server error",
            )}
    }

