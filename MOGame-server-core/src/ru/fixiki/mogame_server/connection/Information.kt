package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Module responsible for sending common information about the game.
 * E.g. players score, categories of question
 * */
@ExperimentalCoroutinesApi
@Suppress("unused")
fun Application.information() {
    routing {
        get(GAME_PACKAGE_INFO) {

        }
        webSocket(ROUND_GAME_TABLE) {

        }
    }
}
