package br.com.devrodrigues.fakewaze.application.http

import br.com.devrodrigues.fakewaze.application.http.handler.GraphApiHandle
import br.com.devrodrigues.fakewaze.application.http.handler.WazeApiHandle
import br.com.devrodrigues.fakewaze.application.http.handler.toServerResponse
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class RouterConfiguration(
    private val wazeApiHandle: WazeApiHandle,
    private val graphApiHandle: GraphApiHandle
):RouterApiDefinition  {
    companion object {
        const val WAZE_ROOT_PATH = "/waze"
        const val GRAPH_PATH = "/graph"
        const val WAZE_TO_PARAMETER = "to"
        const val WAZE_FROM_PARAMETER = "from"
    }

    @Bean
    override fun requestRouter() = coRouter {
        onError<Throwable> { ex, req ->
            withRequestContext(req.context) {
                ex.toServerResponse()
            }
        }
        accept(APPLICATION_JSON).nest {
            WAZE_ROOT_PATH.nest {

                GET("/{${WAZE_FROM_PARAMETER}}/{${WAZE_TO_PARAMETER}}") { req ->
                    withRequestContext(req.context) {
                        wazeApiHandle.getRoute(req)
                    }
                }
            }
        }
    }

    @Bean
    override fun requestGraph(): RouterFunction<ServerResponse> = coRouter {
        onError<Throwable> { ex, req ->
            withRequestContext(req.context) {
                ex.toServerResponse()
            }
        }
        accept(APPLICATION_JSON).nest {
            GRAPH_PATH.nest {
                GET("/") { req ->
                    withRequestContext(req.context) {
                        graphApiHandle.getRoute(req)
                    }
                }
            }
        }
    }
}

//@Configuration
//class CorsConfig {
//    @Bean
//    fun corsWebFilter(): WebFilter {
//        val corsConfig = CorsConfiguration()
//        corsConfig.addAllowedOrigin("*")
//        corsConfig.addAllowedMethod("*")
//        corsConfig.addAllowedHeader("*")
//
//        return CorsWebFilter { _, corsConfigurationSource ->
//            corsConfig
//        }
//    }
//}
//
