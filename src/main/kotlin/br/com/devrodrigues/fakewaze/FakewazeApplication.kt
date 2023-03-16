package br.com.devrodrigues.fakewaze

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class FakewazeApplication

fun main(args: Array<String>) {
    runApplication<FakewazeApplication>(*args)
}