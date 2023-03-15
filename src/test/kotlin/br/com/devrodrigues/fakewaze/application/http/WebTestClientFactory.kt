package br.com.devrodrigues.fakewaze.application.http

import br.com.devrodrigues.fakewaze.application.http.handler.GraphApiHandle
import br.com.devrodrigues.fakewaze.application.http.handler.WazeApiHandle
import kotlinx.serialization.json.Json
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.HandlerStrategies
import java.time.Duration

fun createWebTestClient(
    wazeApiHandle: WazeApiHandle,
    graphApiHandle: GraphApiHandle
): WebTestClient {
    val router = RouterConfiguration(
        wazeApiHandle = wazeApiHandle,
        graphApiHandle = graphApiHandle
    ).requestRouter()
    val json = Json { ignoreUnknownKeys = true }
    val jsonEncoder = KotlinSerializationJsonEncoder(json)
    val jsonDecoder = KotlinSerializationJsonDecoder(json)

    return WebTestClient
        .bindToRouterFunction(router)
        .handlerStrategies(
            HandlerStrategies
                .builder()
                .codecs { codecConfigurator ->
                    codecConfigurator.defaultCodecs().kotlinSerializationJsonEncoder(jsonEncoder)
                    codecConfigurator.defaultCodecs().kotlinSerializationJsonDecoder(jsonDecoder)
                }
                .build()
        )
        .configureClient()
        .responseTimeout(Duration.ofMinutes(1))
        .codecs { codecConfigurator ->
            codecConfigurator.defaultCodecs().kotlinSerializationJsonEncoder(jsonEncoder)
            codecConfigurator.defaultCodecs().kotlinSerializationJsonDecoder(jsonDecoder)
        }
        .build()
}