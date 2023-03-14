package br.com.devrodrigues.fakewaze.core.domain.usecase

import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException.WazeDestinationNotFoundException
import br.com.devrodrigues.fakewaze.core.domain.repositories.NeighborhoodRepository
import br.com.devrodrigues.fakewaze.core.domain.usecase.ShouldGetBestRouteUseCaseFixture.getNeighborhood
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.Double.Companion.MAX_VALUE

class ShouldGetBestRouteUseCaseTest : DescribeSpec({

    val repository = mockk<NeighborhoodRepository>()
    val usecase = mockk<DijkstraUseCase>()

    val shouldGetBestRouteUseCase = ShouldGetBestRouteUseCase(repository, usecase)


    describe("Execute ShouldGetBestRouteUseCase") {
        it("should throw exception when getting neighborhood by nameFrom") {

            every { runBlocking { repository.getNeighborhoodBy("nameFrom") } } throws WazeDestinationNotFoundException(
                "Invalid neighborhood"
            )

            val exception = shouldThrow<WazeDestinationNotFoundException> {
                runBlocking { shouldGetBestRouteUseCase.execute("nameFrom", "nameTo") }
            }

            exception.message shouldBe "Invalid neighborhood"
        }

        it("should throw exception when getting neighborhood by nameTo") {

            every { runBlocking { repository.getNeighborhoodBy("nameFrom") } } returns mockk()

            every { runBlocking { repository.getNeighborhoodBy("nameTo") } } throws WazeDestinationNotFoundException(
                "Invalid neighborhood"
            )

            val exception = shouldThrow<WazeDestinationNotFoundException> {
                runBlocking { shouldGetBestRouteUseCase.execute("nameFrom", "nameTo") }
            }

            exception.message shouldBe "Invalid neighborhood"
        }

        it("should execute calling consult a valid neighborhood then return a valid response") {
            every { runBlocking { repository.getNeighborhoodBy("nameFrom") } } returns getNeighborhood()
            every { runBlocking { repository.getNeighborhoodBy("nameTo") } } returns getNeighborhood()
            every { runBlocking { repository.getNeighborhoodGraph() } } returns mapOf("fake" to mockk())
            every { runBlocking { usecase.execute(any(), any(), any()) } } returns Pair(MAX_VALUE, listOf())


            val result = runBlocking { shouldGetBestRouteUseCase.execute("nameFrom", "nameTo") }

            result shouldNot beNull()
        }
    }
})