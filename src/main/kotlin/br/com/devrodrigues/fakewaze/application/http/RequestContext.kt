package br.com.devrodrigues.fakewaze.application.http

import br.com.devrodrigues.fakewaze.application.http.handler.toServerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

suspend inline fun withRequestContext(
    wazeContext: WazeContext,
    noinline block: suspend CoroutineScope.(WazeContext) -> ServerResponse
): ServerResponse {
    val context = currentWazeContext() + wazeContext

    return withContext(context + MDCContext(context.toSensitiveMap())) {
        try {
            block(context)
        } catch (ex: Throwable) {
            ex.toServerResponse()
        }
    }
}

val ServerRequest.context
    get(): WazeContext =
        this.headers().asHttpHeaders().toSingleValueMap().toWazeContext() + WazeContext(
            origin = this.headers().firstHeader("X-Origin")
        )



fun Map<String, String?>.toWazeContext(): WazeContext {
    return WazeContext(
        origin = this["X-Origin"] ?: this["X-Origin-App"]
    )
}