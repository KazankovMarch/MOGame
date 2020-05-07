package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.fixiki.mogame_server.model.dto.users.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse

/**
 * Module responsible for receiving and sending info about users in the game
 * */
@ExperimentalCoroutinesApi
@Suppress("unused")
fun Application.users() {
    routing {
        webSocket(USERS) {
            val request: RegistrationRequest = incoming.receiveT()
            val subscriptionResponse = game.trySubscribeToUserInfoChannel(request)
            if (subscriptionResponse is UsersInfoSubscriptionResponse.Failure) {
                outgoing.sendT(subscriptionResponse.failureResponse)
                return@webSocket
            }
            with(subscriptionResponse as UsersInfoSubscriptionResponse.Success) {
                outgoing.sendT(subscriptionResponse.successResponse)
                try {
                    launch {
                        initValues.forEach { outgoing.sendT(it) }
                        subscription.consumeEach { outgoing.sendT(it) }
                    }
                    for (message in incoming) {
                        println(message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    subscription.cancel()
                    game.disconnectUser(successResponse.token)
                }
            }
        }
        post(AVATAR) {

        }
    }
}
