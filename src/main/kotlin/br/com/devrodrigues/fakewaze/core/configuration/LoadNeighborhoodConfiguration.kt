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
            val neighborhood = Neighborhood(name = neighborhoodJson.name,
                lat = neighborhoodJson.lat,
                lng = neighborhoodJson.lng,
                neighbors = neighborhoodJson.neighbors.map {
                    Neighborhood(
                        name = it.name, lat = it.lat, lng = it.lng, neighbors = emptyList()
                    )
                }

            )
            neighborhoods[neighborhood.name] = neighborhood
        }

        return neighborhoods
    }

    @Bean
    @Order(2)
    fun loadNeighbored(): Map<String, List<Neighborhood>> {
        return mapOf(
            "Leblon" to listOf(
                this.getNeighborhoodData()["Ipanema"]!!,
                this.getNeighborhoodData()["Gavea"]!!,
                this.getNeighborhoodData()["Copacabana"]!!
            ),
            "Ipanema" to listOf(this.getNeighborhoodData()["Leblon"]!!, this.getNeighborhoodData()["Gavea"]!!),
            "Copacabana" to listOf(
                this.getNeighborhoodData()["Ipanema"]!!,
                this.getNeighborhoodData()["Botafogo"]!!,
                this.getNeighborhoodData()["Flamengo"]!!
            ),
            "Botafogo" to listOf(
                this.getNeighborhoodData()["Copacabana"]!!,
                this.getNeighborhoodData()["Flamengo"]!!,
                this.getNeighborhoodData()["Catete"]!!
            ),
            "Flamengo" to listOf(
                this.getNeighborhoodData()["Copacabana"]!!,
                this.getNeighborhoodData()["Botafogo"]!!,
                this.getNeighborhoodData()["Catete"]!!,
                this.getNeighborhoodData()["Glória"]!!
            ),
            "Catete" to listOf(
                this.getNeighborhoodData()["Botafogo"]!!,
                this.getNeighborhoodData()["Flamengo"]!!,
                this.getNeighborhoodData()["Catete"]!!,
                this.getNeighborhoodData()["Laranjeiras"]!!,
            ),
            "Glória" to listOf(this.getNeighborhoodData()["Lapa"]!!, this.getNeighborhoodData()["Flamengo"]!!),
            "Laranjeiras" to listOf(
                this.getNeighborhoodData()["Catete"]!!,
                this.getNeighborhoodData()["Jardim Botânico"]!!
            ),
            "Jardim Botânico" to listOf(
                this.getNeighborhoodData()["Laranjeiras"]!!,
                this.getNeighborhoodData()["Lagoa"]!!
            ),
            "Lagoa" to listOf(
                this.getNeighborhoodData()["Jardim Botânico"]!!, this.getNeighborhoodData()["Gavea"]!!
            ),
            "Gavea" to listOf(this.getNeighborhoodData()["Leblon"]!!, this.getNeighborhoodData()["Ipanema"]!!)
        )
    }

    @Bean
    @Order(3)
    fun getNeighborhoodGraph(): Map<String, Neighborhood> {
        val neighborhoods = this.getNeighborhoodData()

        val response = mutableMapOf<String, Neighborhood>()

        neighborhoods.forEach { (name, neighborhood) ->
            val neighbors = loadNeighbored()[name] ?: emptyList()
            val neighborhoodWithNeighbors = neighborhood.copy(
                neighbors = neighbors.map { it },
            )
            response[name] = neighborhoodWithNeighbors
        }

        return response
    }

    companion object {
        const val NEIGHBORHOOD_FILE_NAME = "neighborhoods.json"
        val LOGGER = KotlinLogging.logger { }
    }
}