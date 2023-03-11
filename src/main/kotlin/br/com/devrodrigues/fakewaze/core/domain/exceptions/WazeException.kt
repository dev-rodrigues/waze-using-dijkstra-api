package br.com.devrodrigues.fakewaze.core.domain.exceptions

sealed class WazeException(message: String): Exception(message) {

    class WazeNotFoundException(message: String): WazeException(message)

    class WazeClassPathResourceNotFoundException(message: String): WazeException(message)
}