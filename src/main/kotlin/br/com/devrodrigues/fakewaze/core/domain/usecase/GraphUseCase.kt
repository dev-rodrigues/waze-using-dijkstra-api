package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import br.com.devrodrigues.fakewaze.core.domain.repositories.NeighborhoodRepository
import org.springframework.stereotype.Component

@Component
class GraphUseCase(
    private val neighborhoodRepository: NeighborhoodRepository
) {
    suspend fun getData(): Map<String, Neighborhood> {
        return neighborhoodRepository.getNeighborhoodGraph()
    }
}