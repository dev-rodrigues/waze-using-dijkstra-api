package br.com.devrodrigues.fakewaze.application.http

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

suspend inline fun currentWazeContext(): WazeContext = coroutineContext[WazeContext.Key] ?: WazeContext()
data class WazeContext(
    val origin: String? = null
) : AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<WazeContext>

    override operator fun plus(context: CoroutineContext) = when (context) {
        is WazeContext -> WazeContext(
            origin = context.origin ?: origin
        )

        else -> super.plus(context)
    }

    operator fun plus(context: WazeContext) = WazeContext(
        origin = context.origin ?: origin
    )
}

fun CoroutineContext.toSensitiveMap(): Map<String, String> = mapOf(
    "origin" to (this[WazeContext.Key]?.origin ?: "null")
)
