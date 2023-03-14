package br.com.devrodrigues.fakewaze.application.http

import org.springframework.test.web.reactive.server.WebTestClient

fun <T : WebTestClient.RequestHeadersSpec<T>> WebTestClient.RequestHeadersSpec<T>.origin() =
    this.header("X-Origin", "test")