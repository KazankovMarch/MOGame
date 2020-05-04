package ru.fixiki.ru.fixiki.mogame_server.connection

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import java.time.*
import com.fasterxml.jackson.databind.*
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.receive
import kotlinx.coroutines.delay
import ru.fixiki.mogame_server.core.GameImpl
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.unpacking.GamePackageLoader
import java.lang.Exception

const val REGISTRATION_PATH = "/registration"
const val GAME_FOLDER_PROPERTY = "game.folder"

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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
    val game = GameImpl(gamePackage)

    routing {
        post(REGISTRATION_PATH) {
            try {
                val user = call.receive<User.Info>()
                call.respond(game.tryRegisterUser(user))
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        webSocket("/users") {
            val uuid = (incoming.receive() as? Frame.Text)?.readText()
            if (uuid == null || game.isValidUuid(uuid)) {
                close(reason = CloseReason(CloseReason.Codes.VIOLATED_POLICY, "invalid UUID"))
                return@webSocket
            }
            val changesQueue = game.newUserChangesQueue(uuid)
            while (!outgoing.isClosedForSend || !incoming.isClosedForReceive) {
                delay(1_000)
                if (changesQueue.isNotEmpty()) {
                    call.respond(changesQueue.remove())
                }
            }
        }
        webSocket("/game") { // websocketSession

        }
    }
}

