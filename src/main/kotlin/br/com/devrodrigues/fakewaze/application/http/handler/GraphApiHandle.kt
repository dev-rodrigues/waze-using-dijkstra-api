package br.com.devrodrigues.fakewaze.application.http.handler

import br.com.devrodrigues.fakewaze.core.domain.usecase.GraphUseCase
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class GraphApiHandle(
    private val useCase: GraphUseCase
) {
    suspend fun getRoute(request: ServerRequest): ServerResponse {
        val result = useCase.getData()

        return ServerResponse.ok().bodyValueAndAwait(
            body = result
        )
    }
}