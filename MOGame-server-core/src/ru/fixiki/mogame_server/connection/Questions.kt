package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.websocket.webSocket


/**
 * Module responsible for sending and receiving data about current question in game
 * */
@Suppress("unused")
fun Application.questions() {
    routing {
        post(QUESTION_SELECT) {

        }
        webSocket(CURRENT_QUESTION_INFO) {

        }
        webSocket(SCENARIO) {

        }
        post(ANSWER_BUTTON) {

        }
        webSocket(CORRECT_ANSWERS) {

        }
    }
}

/**
 * Module for bag-cat question type
 * */
@Suppress("unused")
fun Application.bagCat() {
    routing {
        post(BAG_CAT_CHOICE) {

        }
        webSocket(SHOW_BAG_CAT_CHOICE) {

        }
    }
}

/**
 * Module for auction question type
 * */
@Suppress("unused")
fun Application.auction() {
    routing {
        webSocket(AUCTION_ORDER) {

        }
        post(AUCTION_BET) {

        }
        webSocket(SHOW_AUCTION_BET) {

        }
    }
}