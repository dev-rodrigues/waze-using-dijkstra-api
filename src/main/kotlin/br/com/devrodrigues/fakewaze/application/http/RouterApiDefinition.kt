package br.com.devrodrigues.fakewaze.application.http

import br.com.devrodrigues.fakewaze.application.http.RouterConfiguration.Companion.WAZE_FROM_PARAMETER
import br.com.devrodrigues.fakewaze.application.http.RouterConfiguration.Companion.WAZE_ROOT_PATH
import br.com.devrodrigues.fakewaze.application.http.RouterConfiguration.Companion.WAZE_TO_PARAMETER
import br.com.devrodrigues.fakewaze.application.http.handler.dto.BestRouteResponse
import br.com.devrodrigues.fakewaze.application.http.handler.dto.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

interface RouterApiDefinition {

    @RouterOperations(
        RouterOperation(
            path = "$WAZE_ROOT_PATH/{${WAZE_TO_PARAMETER}}/{${WAZE_FROM_PARAMETER}}",
            method = [GET],
            operation = Operation(
                operationId = "get-best-route",
                summary = "get best route to go from origin to destination",
                tags = ["Waze"],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        content = [
                            Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = Schema(implementation = BestRouteResponse::class),
                                examples = [
                                    ExampleObject(
                                        name = "best-route",
                                        value = """
                                            {
                                                "origin": "A",
                                                "destination": "D",
                                                "distance": 10.0,
                                                "route": ["A", "B", "D"]
                                            }
                                        """
                                    )
                                ]
                            )
                        ]
                    ),
                    ApiResponse(
                        responseCode = "500",
                        content = [
                            Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = Schema(implementation = ErrorResponse::class),
                                examples = [
                                    ExampleObject(
                                        name = "error",
                                        value = """
                                            {
                                                "message": "Internal Server Error"
                                            }
                                        """
                                    )
                                ]
                            )
                        ]
                    )
                ]
            )
        )
    )
    fun requestRouter(): RouterFunction<ServerResponse>

    fun requestGraph(): RouterFunction<ServerResponse>
}