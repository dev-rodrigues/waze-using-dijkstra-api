package br.com.devrodrigues.fakewaze.core.configuration


import br.com.devrodrigues.fakewaze.core.configuration.dto.NeighborhoodJson
import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException.WazeClassPathResourceNotFoundException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource


@Configuration
class LoadNeighborhoodConfiguration(
    private val mapper: ObjectMapper
) {

    private suspend fun getBasePath(): String {
        val resource = ClassPathResource("/data")

        if (!resource.exists()) {
            throw WazeClassPathResourceNotFoundException("Image resource not found")
        }

        return resource.path + "/"
    }

    @Bean
    suspend fun loadNeighborhoods(): Map<String, Neighborhood> {

        val resource = ClassPathResource(getBasePath() + NEIGHBORHOOD_FILE_NAME)
        val jsonString = resource.inputStream.bufferedReader().use { it.readText() }
        val neighborhoods = mutableMapOf<String, Neighborhood>()


        val neighborhoodsList = mapper.readValue(jsonString, object : TypeReference<List<NeighborhoodJson>>() {})

        LOGGER.info { "neighborhoodsList: $neighborhoodsList" }

        neighborhoodsList.forEach { neighborhoodJson ->
            val neighborhood = Neighborhood(name = neighborhoodJson.name,
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

    companion object {
        const val NEIGHBORHOOD_FILE_NAME = "neighborhoods.json"
        val LOGGER = KotlinLogging.logger { }
    }
}