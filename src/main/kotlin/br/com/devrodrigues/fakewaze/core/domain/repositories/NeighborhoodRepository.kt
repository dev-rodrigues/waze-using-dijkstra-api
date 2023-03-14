package br.com.devrodrigues.fakewaze.core.domain.repositories

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood

interface NeighborhoodRepository {
    suspend fun getNeighborhoodBy(name: String): Neighborhood
    suspend fun getNeighborhoodGraph(): Map<String, Neighborhood>
}