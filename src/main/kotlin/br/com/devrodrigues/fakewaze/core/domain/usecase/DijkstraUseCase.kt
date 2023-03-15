package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood
import br.com.devrodrigues.fakewaze.core.domain.utils.DistanceUtil.distance
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
        end: Neighborhood,
        maxDistance: Double = 5000.0,
        maxNodes: Int = 10
    ): Pair<Double, List<Neighborhood>> {

        val visited = mutableSetOf<Neighborhood>()
        val distances = mutableMapOf<Neighborhood, Double>()
        val predecessors = mutableMapOf<Neighborhood, Neighborhood?>()
        val queue = PriorityQueue<Neighborhood>(compareBy { distances.getOrDefault(it, Double.MAX_VALUE) })

        queue.add(start)
        distances[start] = 0.0

        var nodesVisited = 0
        var pathFound = false

        while (queue.isNotEmpty() && nodesVisited < maxNodes) {
            val current = queue.poll()
            visited.add(current)

            if (current == end) {
                pathFound = true
                break
            }

            for (neighbor in graph[current.name]?.neighbors ?: emptyList()) {
                if (neighbor in visited) {
                    continue
                }

                val tentativeDistance = distances.getOrDefault(current, Double.MAX_VALUE) + distance(current, neighbor)
                if (tentativeDistance <= maxDistance) {
                    if (tentativeDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                        distances[neighbor] = tentativeDistance
                        predecessors[neighbor] = current
                        queue.add(neighbor)
                    }
                }
            }
            nodesVisited++
        }

        if (!pathFound) {
            return Pair(Double.POSITIVE_INFINITY, emptyList())
        }

        val path = mutableListOf<Neighborhood>()
        var current: Neighborhood? = end

        while (current != null) {
            path.add(current)
            current = predecessors[current]
        }

        path.reverse()

        return Pair(distances[end]!!, path)
    }
}