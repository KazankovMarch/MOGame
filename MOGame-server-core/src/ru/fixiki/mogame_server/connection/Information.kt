package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.receiveOrNull
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse

/**
 * Module responsible for sending common information about the game.
 * E.g. players score, categories of question
 * */
@ExperimentalCoroutinesApi
@Suppress("unused")
fun Application.information() {
    routing {
        webSocket(USERS) {
            val token = (incoming.receive() as? Frame.Text)?.readText()
            if (token == null || !game.tokenIsValid(token)) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "invalid token"))
                return@webSocket
            }
            var subscriptionResponse: UsersInfoSubscriptionResponse? = null
            try {
                subscriptionResponse = game.subscribeToUsersInfo(token)
                run {
                    subscriptionResponse.initValues.forEach { outgoing.sendT(it) }
                    subscriptionResponse.subscription.consumeEach { outgoing.sendT(it) }
                }
                while (true) {
                    incoming.receiveOrNull() ?: break
                }
            } finally {
                game.disconnectUser(token)
                subscriptionResponse?.subscription?.cancel()
            }
        }
        get(GAME_PACKAGE_INFO) {

        }
        webSocket(ROUND_GAME_TABLE) {

        }
    }
}
