package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Component
class DijkstraUseCase {
    suspend fun execute(
        graph: Map<String, Neighborhood>,
        start: Neighborhood,
        end: Neighborhood
    ): Pair<Double, List<String>> {
        val visited = mutableSetOf<Neighborhood>()
        val distances = mutableMapOf<Neighborhood, Double>()
        val predecessors = mutableMapOf<Neighborhood, Neighborhood?>()
        val queue = PriorityQueue<Neighborhood>(compareBy { distances.getOrDefault(it, Double.MAX_VALUE) })

        queue.add(graph[start.name])
        distances[start] = 0.0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            visited.add(current)

            if (current == end) {
                break
            }

            for (neighbor in current.neighbors) {
                if (neighbor in visited) {
                    continue
                }

                val tentativeDistance = if (distances[current] != null) distances[current] else 0 + distance(current, neighbor)
                if (tentativeDistance != null) {
                    if (tentativeDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                        distances[neighbor] = tentativeDistance
                        predecessors[neighbor] = current
                        if (!queue.contains(neighbor)) {
                            queue.add(neighbor)
                        }
                    }
                }
            }
        }

        if (end !in visited) {
            return Pair(Double.POSITIVE_INFINITY, emptyList())
        }

        val path = mutableListOf<String>()
        var current: Neighborhood? = end
        while (current != null) {
            path.add(current.name)
            current = predecessors[current]
        }
        path.reverse()

        return Pair(distances[end]!!, path)
    }

    // Usar a fórmula Haversine para calcular a distância entre as coordenadas lat/long dos bairros
    suspend fun distance(from: Neighborhood, to: Neighborhood): Double {
        val lat1 = from.lat.toDouble()
        val lon1 = from.lng.toDouble()
        val lat2 = to.lat.toDouble()
        val lon2 = to.lng.toDouble()
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R_KM * c
    }

    companion object {
        const val R_KM = 6371
    }
}