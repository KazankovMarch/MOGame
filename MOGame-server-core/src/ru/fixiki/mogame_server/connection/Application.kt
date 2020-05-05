package ru.fixiki.ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.Cookie
import io.ktor.http.cio.websocket.*
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.webSocket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import ru.fixiki.mogame_server.connection.REGISTRATION
import ru.fixiki.mogame_server.connection.START
import ru.fixiki.mogame_server.connection.USERS
import ru.fixiki.mogame_server.core.Game
import ru.fixiki.mogame_server.core.GameImpl
import ru.fixiki.mogame_server.model.dto.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.unpacking.GamePackageLoader
import java.time.Duration

lateinit var game: Game

val objectMapper = jacksonObjectMapper()

const val GAME_FOLDER_PROPERTY = "game.folder"

const val TOKEN_COOKIE_NAME = "mogame_token"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.mainModule(testing: Boolean = false) {
    install(io.ktor.websocket.WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val gameFolder = environment.config.property(GAME_FOLDER_PROPERTY).getString()
    val gamePackage = GamePackageLoader.loadPackage(gameFolder)
    game = GameImpl(gamePackage)

    routing {
        post(REGISTRATION) {
            try {
                val request = call.receive<RegistrationRequest>()
                val response = game.tryRegisterUser(request)
                if (response is RegistrationResponse.Success) {
                    call.response.cookies.append(Cookie(name = TOKEN_COOKIE_NAME, value = response.token))
                }
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        webSocket(USERS) {
            val token = (incoming.receive() as? Frame.Text)?.readText()
            if (token == null || !game.isTokenValid(token)) {
                close(reason = CloseReason(CloseReason.Codes.VIOLATED_POLICY, "invalid token"))
                return@webSocket
            }
            val changesQueue = game.usersUpdatesQueue(token)
            while (incoming.isEmpty || incoming.receive() !is Frame.Close) {
                delay(100)
                while (changesQueue.isNotEmpty()) {
                    outgoing.send(Frame.Text(objectMapper.writeValueAsString(changesQueue.remove())))
                }
            }
        }
        post(START) {

        }
        webSocket {

        }
    }
}

