package br.com.devrodrigues.fakewaze.core.configuration.dto

import kotlinx.serialization.Serializable

@Serializable
data class NeighborhoodJson(
    val name: String,
    val lat: String,
    val lng: String,
    val weight: Double,
    val neighbors: List<NeighborhoodJson>
)