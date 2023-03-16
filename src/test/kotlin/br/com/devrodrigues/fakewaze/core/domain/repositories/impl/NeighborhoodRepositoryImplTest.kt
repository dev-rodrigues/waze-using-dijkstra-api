package br.com.devrodrigues.fakewaze.core.domain.repositories.impl

import br.com.devrodrigues.fakewaze.core.domain.exceptions.WazeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking


class NeighborhoodRepositoryImplTest : DescribeSpec({

    val repository = NeighborhoodRepositoryImpl(
        getNeighborhoodData = NeighborhoodRepositoryFixture.getNeighborhoodData(),
        getNeighborhoodGraph = NeighborhoodRepositoryFixture.getNeighborhoodData(),

    )

    describe("Should execute NeighborhoodRepositoryImpl.getNeighborhoodByName") {
        it("should throw WazeDestinationNotFoundException when getting neighborhood by name") {
            val exception = shouldThrow<WazeException.WazeDestinationNotFoundException> {
                runBlocking { repository.getNeighborhoodBy("invalid data") }
            }

            exception.message shouldBe "Invalid neighborhood"
        }

        it("should return neighborhood when getting neighborhood by name") {
            val neighborhood = runBlocking { repository.getNeighborhoodBy("fake") }

            neighborhood.name shouldBe "fake"
        }
    }

    describe("Should execute NeighborhoodRepositoryImpl.getNeighborhoodGraph") {
        it("should return neighborhood graph") {
            val neighborhoodGraph = runBlocking { repository.getNeighborhoodGraph() }

            neighborhoodGraph.size shouldBe 1
        }
    }
})