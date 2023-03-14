package br.com.devrodrigues.fakewaze.application.http.handler

import br.com.devrodrigues.fakewaze.application.http.createWebTestClient
import br.com.devrodrigues.fakewaze.application.http.handler.WazeApiHandleFixture.getBestRouteResponse
import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException
import br.com.devrodrigues.fakewaze.core.domain.usecase.ShouldGetBestRouteUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import org.springframework.http.MediaType.APPLICATION_JSON

class WazeApiHandleTest : DescribeSpec({


    val shouldGetBestRouteUseCase = mockk<ShouldGetBestRouteUseCase>()

    val webClient = createWebTestClient(
        wazeApiHandle = WazeApiHandle(
            shouldGetBestRouteUseCase
        )
    )

    describe("Should execute WazeApiHandle") {
        it("Should return 200 when pass two valid parameters") {

            coEvery {
                shouldGetBestRouteUseCase.execute(any(), any())
            } returns getBestRouteResponse()

            webClient
                .get()
                .uri { builder ->
                    builder
                        .path("/waze/nameFrom/nameTo")
                        .build()
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is2xxSuccessful

            coVerify {
                shouldGetBestRouteUseCase.execute(any(), any())
            }

            confirmVerified(shouldGetBestRouteUseCase)
        }

        it("Should return 400 when pass invalid parameters") {
            coEvery {
                shouldGetBestRouteUseCase.execute(any(), any())
            } throws WazeException.WazeDestinationNotFoundException("Invalid neighborhood")

            webClient
                .get()
                .uri { builder ->
                    builder
                        .path("/waze/invalidNameFrom/invalidNameTo")
                        .build()
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError

            coVerify {
                shouldGetBestRouteUseCase.execute(any(), any())
            }

            confirmVerified(shouldGetBestRouteUseCase)
        }
    }

})