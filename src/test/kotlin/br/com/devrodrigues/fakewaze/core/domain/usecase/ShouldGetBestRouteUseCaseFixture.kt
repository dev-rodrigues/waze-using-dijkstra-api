package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood

object ShouldGetBestRouteUseCaseFixture {

    fun getNeighborhood(
        name: String = "name",
        lat: String = "lat",
        lng: String = "lng",
        neighbors: List<Neighborhood> = emptyList()
    ) = Neighborhood(
        name = name,
        lat = lat,
        lng = lng,
        neighbors = neighbors
    )
}