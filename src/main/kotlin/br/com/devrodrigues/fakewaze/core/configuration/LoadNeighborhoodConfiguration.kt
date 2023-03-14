package br.com.devrodrigues.fakewaze.core.configuration


import br.com.devrodrigues.fakewaze.core.configuration.dto.NeighborhoodJson
import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException.WazeClassPathResourceNotFoundException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource


@Configuration
class LoadNeighborhoodConfiguration(
    private val mapper: ObjectMapper
) {

    private fun getBasePath(): String {
        val resource = ClassPathResource("/data")

        if (!resource.exists()) {
            throw WazeClassPathResourceNotFoundException("Image resource not found")
        }

        return resource.path + "/"
    }

    @Bean
    @Order(1)
    fun getNeighborhoodData(): Map<String, Neighborhood> {
        val resource = ClassPathResource(getBasePath() + NEIGHBORHOOD_FILE_NAME)
        val jsonString = resource.inputStream.bufferedReader().use { it.readText() }
        val neighborhoods = mutableMapOf<String, Neighborhood>()


        val neighborhoodsList = mapper.readValue(jsonString, object : TypeReference<List<NeighborhoodJson>>() {})

        LOGGER.info { "neighborhoodsList: $neighborhoodsList" }

        neighborhoodsList.forEach { neighborhoodJson ->
            val neighborhood = Neighborhood(
                name = neighborhoodJson.name,
                lat = neighborhoodJson.lat,
                lng = neighborhoodJson.lng,
                weight = neighborhoodJson.weight,
                neighbors = neighborhoodJson.neighbors.map {
                    Neighborhood(
                        name = it.name, lat = it.lat, lng = it.lng, weight = it.weight, neighbors = emptyList()
                    )
                }

            )
            neighborhoods[neighborhood.name] = neighborhood
        }

        return neighborhoods
    }

    @Bean
    @Order(2)
    fun loadNeighbord(): Map<String, List<Pair<Neighborhood, Int>>> {
        return mapOf(
            "Leblon" to listOf(
                Pair(getNeighborhoodData()["Ipanema"]!!, 2),
                Pair(getNeighborhoodData()["Gavea"]!!, 5),
            ),
            "Ipanema" to listOf(
                Pair(getNeighborhoodData()["Leblon"]!!, 2),
                Pair(getNeighborhoodData()["Gavea"]!!, 3),
                Pair(getNeighborhoodData()["Copacabana"]!!, 4),
            ),
            "Copacabana" to listOf(
                Pair(getNeighborhoodData()["Ipanema"]!!, 4),
                Pair(getNeighborhoodData()["Botafogo"]!!, 3),
                Pair(getNeighborhoodData()["Flamengo"]!!, 5),
            ),
            "Botafogo" to listOf(
                Pair(getNeighborhoodData()["Copacabana"]!!, 3),
                Pair(getNeighborhoodData()["Flamengo"]!!, 2),
                Pair(getNeighborhoodData()["Catete"]!!, 2),
            ),
            "Flamengo" to listOf(
                Pair(getNeighborhoodData()["Copacabana"]!!, 5),
                Pair(getNeighborhoodData()["Botafogo"]!!, 2),
                Pair(getNeighborhoodData()["Catete"]!!, 2),
                Pair(getNeighborhoodData()["Glória"]!!, 2),
            ),
            "Catete" to listOf(
                Pair(getNeighborhoodData()["Botafogo"]!!, 2),
                Pair(getNeighborhoodData()["Flamengo"]!!, 2),
                Pair(getNeighborhoodData()["Catete"]!!, 2),
                Pair(getNeighborhoodData()["Laranjeiras"]!!, 3),
            ),
            "Glória" to listOf(
                Pair(getNeighborhoodData()["Lapa"]!!, 3),
                Pair(getNeighborhoodData()["Flamengo"]!!, 2),
            ),
            "Laranjeiras" to listOf(
                Pair(getNeighborhoodData()["Catete"]!!, 3),
                Pair(getNeighborhoodData()["Jardim Botânico"]!!, 5),
            ),
            "Jardim Botânico" to listOf(
                Pair(getNeighborhoodData()["Laranjeiras"]!!, 5),
                Pair(getNeighborhoodData()["Lagoa"]!!, 4),
            ),
            "Lagoa" to listOf(
                Pair(getNeighborhoodData()["Jardim Botânico"]!!, 4),
                Pair(getNeighborhoodData()["Gavea"]!!, 3),
            ),
            "Gavea" to listOf(
                Pair(getNeighborhoodData()["Leblon"]!!, 5),
                Pair(getNeighborhoodData()["Ipanema"]!!, 3),
            ),
        )
    }

    @Bean
    @Order(3)
    fun getNeighborhoodDataorhoods(): Map<String, Neighborhood> {
        val neighborhoods = getNeighborhoodData()

        val response = mutableMapOf<String, Neighborhood>()

        neighborhoods.forEach { (name, neighborhood) ->
            val neighbors = loadNeighbord()[name] ?: emptyList()
            val neighborhoodWithNeighbors = neighborhood.copy(neighbors = neighbors.map { it.first })
            response[name] = neighborhoodWithNeighbors
        }

        return response
    }

    companion object {
        const val NEIGHBORHOOD_FILE_NAME = "neighborhoods.json"
        val LOGGER = KotlinLogging.logger { }
    }
}