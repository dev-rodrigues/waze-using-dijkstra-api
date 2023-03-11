package br.com.devrodrigues.fakewaze.application.http.handler.dto

data class BestRouteResponse(
    val origin: String,
    val destination: String,
    val distance: Double,
    val route: List<String>
)

data class ErrorResponse(
    val message: String
)

enum class ErrorCode(val messageKey: String, val reasonKey: String? = null) {
    CODE_001("errors.message.code-001"),
}
