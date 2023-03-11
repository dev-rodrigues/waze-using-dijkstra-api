package br.com.devrodrigues.fakewaze

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FakewazeApplication

fun main(args: Array<String>) {
    runApplication<FakewazeApplication>(*args)
}
