package br.com.devrodrigues.fakewaze.application.http.handler

import br.com.devrodrigues.fakewaze.application.http.RouterConfiguration.Companion.WAZE_FROM_PARAMETER
import br.com.devrodrigues.fakewaze.application.http.RouterConfiguration.Companion.WAZE_TO_PARAMETER
import br.com.devrodrigues.fakewaze.core.domain.usecase.ShouldGetBestRouteUseCase
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class WazeApiHandle(
    private val useCase: ShouldGetBestRouteUseCase
) {
    suspend fun getRoute(request: ServerRequest):ServerResponse {

        val from = request.pathVariable(WAZE_FROM_PARAMETER)
        val to = request.pathVariable(WAZE_TO_PARAMETER)

        LOGGER.info { "to: $to, from: $from" }

        return ServerResponse.ok().bodyValueAndAwait(useCase.execute(
            from = from,
            to = to
        ))
    }

    companion object {
        val LOGGER = KotlinLogging.logger { }
    }
}