package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.application.http.handler.dto.BestRouteResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class ShouldGetBestRouteUseCase {
    suspend fun execute(from: String, to: String): BestRouteResponse {
        return BestRouteResponse(
            origin = from,
            destination = to,
            distance = 10.0,
            route = listOf(from, to)
        )
    }

    companion object {
        val LOGGER = KotlinLogging.logger { }
    }
}