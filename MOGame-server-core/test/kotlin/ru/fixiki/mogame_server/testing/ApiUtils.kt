package ru.fixiki.mogame_server.testing

import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import ru.fixiki.mogame_server.connection.USERS
import ru.fixiki.mogame_server.model.User


fun TestApplicationEngine.handleUsersWebSocketConversation(
    nickname: String,
    role: User.Role,
    token: String? = null,
    callback: (suspend TestApplicationCall.(incoming: ReceiveChannel<Frame>, outgoing: SendChannel<Frame>) -> Unit)? = null
) = handleWebSocketConversation(USERS) { incoming, outgoing ->
    val message = if (token == null)
        """{
            "nickname": "$nickname", 
            "role": "$role"
        }""".trimIndent()
    else
        """{
            "nickname": "$nickname", 
            "role": "$role",
            "token": "$token"
        }""".trimIndent()

    outgoing.send(
        Frame.Text(message)
    )
    callback?.invoke(this, incoming, outgoing)
}