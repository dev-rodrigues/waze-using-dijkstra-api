package br.com.devrodrigues.fakewaze.core.domain.repositories.impl

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException.WazeDestinationNotFoundException
import br.com.devrodrigues.fakewaze.core.domain.repositories.NeighborhoodRepository
import org.springframework.stereotype.Component

@Component
class NeighborhoodRepositoryImpl(
    private var getNeighborhoodData: Map<String, Neighborhood>,
    private var getNeighborhoodDataorhoods: Map<String, Neighborhood>,
): NeighborhoodRepository {
    override suspend fun getNeighborhoodBy(name: String): Neighborhood =
        getNeighborhoodData[name] ?: throw WazeDestinationNotFoundException("Invalid neighborhood")

    override suspend fun getNeighborhoodGraph(): Map<String, Neighborhood> =
        getNeighborhoodDataorhoods
}