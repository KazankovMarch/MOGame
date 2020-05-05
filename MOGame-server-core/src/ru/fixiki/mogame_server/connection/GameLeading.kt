package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.routing.post
import io.ktor.routing.routing

/**
 * Module responsible for controlling game by game lead
 * */
@Suppress("unused")
fun Application.gameLeading() {
    routing {
        post(START) {

        }
        post(PAUSE) {

        }
        post(UNPAUSE) {

        }
        post(EDIT_SCORE) {

        }
    }
}