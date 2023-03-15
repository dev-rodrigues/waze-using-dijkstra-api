package br.com.devrodrigues.fakewaze.core.domain.utils

import br.com.devrodrigues.fakewaze.core.domain.Neighborhood

object DistanceUtil {

    private const val R = 6371

    // Usar a fórmula Haversine para calcular a distância entre as coordenadas lat/long dos bairros
    fun distance(from: Neighborhood, to: Neighborhood): Double {
        val lat1 = from.lat.toDouble()
        val lon1 = from.lng.toDouble()
        val lat2 = to.lat.toDouble()
        val lon2 = to.lng.toDouble()
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
}