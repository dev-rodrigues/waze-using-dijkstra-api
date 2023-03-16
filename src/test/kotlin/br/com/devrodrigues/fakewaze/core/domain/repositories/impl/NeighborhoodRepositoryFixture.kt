package br.com.devrodrigues.fakewaze.core.domain.repositories.impl

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood

object NeighborhoodRepositoryFixture {
    fun getNeighborhoodData(
        name: String = "fake",
        lat: String = "fake",
        lng: String = "fake",
        neighbors: List<Neighborhood> = listOf()
    ) = mapOf(
        "fake" to Neighborhood(
            name = name,
            lat = lat,
            lng = lng,
            neighbors = neighbors
        )
    )
}