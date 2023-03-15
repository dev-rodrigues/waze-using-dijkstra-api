package br.com.devrodrigues.fakewaze.core.domain

data class Neighborhood(
    val name: String,
    val lat: String,
    val lng: String,
    var neighbors: List<Neighborhood> = mutableListOf()
)