package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.application.http.handler.dto.BestRouteResponse
import br.com.devrodrigues.fakewaze.core.domain.repositories.NeighborhoodRepository
import org.springframework.stereotype.Component

@Component
class ShouldGetBestRouteUseCase(
    private val neighborhoodRepository: NeighborhoodRepository,
    private val dijkstraUseCase: DijkstraUseCase
) {
    suspend fun execute(nameFrom: String, nameTo: String): BestRouteResponse {
        val localizedFrom = neighborhoodRepository.getNeighborhoodBy(nameFrom)
        val localizedTo = neighborhoodRepository.getNeighborhoodBy(nameTo)
        val graph = neighborhoodRepository.getNeighborhoodGraph()

        val (distance, route) = dijkstraUseCase.execute(
            graph = graph,
            start = localizedFrom,
            end = localizedTo
        )

        return BestRouteResponse(
            origin = localizedFrom.name,
            destination = localizedTo.name,
            distance = distance,
            route = route
        )
    }
}