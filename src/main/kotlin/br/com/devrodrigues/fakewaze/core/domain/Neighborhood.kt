package br.com.devrodrigues.fakewaze.core.domain

data class Neighborhood(
    val name: String,
    val lat: String,
    val lng: String,
    var weight: Int?=0,
    var neighbors: List<Neighborhood> = mutableListOf()
) {
    fun addNeighbor(neighbor: Neighborhood, weight: Int) {
        neighbors?.plus(neighbor)
        this.weight = weight
    }

    fun listNeighbors(): List<Neighborhood> {
        return neighbors?: emptyList()
    }

    fun getWeight(): Int {
        return weight?:0
    }

    override fun toString(): String {
        return "Neighborhood(name='$name', lat='$lat', lng='$lng', weight=$weight, neighbors=$neighbors)"
    }
}