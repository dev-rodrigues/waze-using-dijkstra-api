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
    ): Pair<Double, List<String>> {
        val visited = mutableSetOf<Neighborhood>()
        val distances = mutableMapOf<Neighborhood, Double>()
        val predecessors = mutableMapOf<Neighborhood, Neighborhood?>()
        val queue = PriorityQueue<Neighborhood>(compareBy { distances.getOrDefault(it, Double.MAX_VALUE) })

        queue.add(graph[start.name])
        distances[start] = 0.0

        var nodesVisited = 0

        while (queue.isNotEmpty() && nodesVisited < maxNodes) {
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
                if (tentativeDistance != null && tentativeDistance <= maxDistance) {
                    if (tentativeDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                        distances[neighbor] = tentativeDistance
                        predecessors[neighbor] = current
                        if (!queue.contains(neighbor)) {
                            queue.add(neighbor)
                        }
                    }
                }
            }
            nodesVisited++
        }

        if (checkIfShouldReturnPositiveInfinity(end, visited)) {
            return shouldReturnPositiveInfinity()
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

    private suspend fun shouldReturnPositiveInfinity(): Pair<Double, List<String>> {
        return Pair(Double.POSITIVE_INFINITY, emptyList())
    }

    private suspend fun checkIfShouldReturnPositiveInfinity(end: Neighborhood, visited: Set<Neighborhood>): Boolean {
        return end !in visited
    }


}