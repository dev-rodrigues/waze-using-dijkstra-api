package br.com.devrodrigues.fakewaze.application.http.handler

import br.com.devrodrigues.fakewaze.application.http.handler.dto.BestRouteResponse

object WazeApiHandleFixture {
    fun getBestRouteResponse(
        origin: String = "fakeOrigin",
        destination: String = "fakeDestination",
        distance: Double = Double.MAX_VALUE,
        route: List<String> = listOf()
    ): BestRouteResponse {
        return BestRouteResponse(
            origin = origin,
            destination = destination,
            distance = distance,
            route = route
        )
    }
}