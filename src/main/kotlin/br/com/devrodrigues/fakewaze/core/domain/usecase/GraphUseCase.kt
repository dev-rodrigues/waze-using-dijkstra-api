package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.application.http.handler.dto.GraphResponse
import br.com.devrodrigues.fakewaze.core.domain.repositories.NeighborhoodRepository
import org.springframework.stereotype.Component

@Component
class GraphUseCase(
    private val neighborhoodRepository: NeighborhoodRepository
) {
    suspend fun getData(): List<GraphResponse> {
        val result = neighborhoodRepository.getNeighborhoodGraph()
        return result.entries.map {
            GraphResponse.map(it.value)
        }
    }
}