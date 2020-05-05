package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.cio.websocket.pingPeriod
import io.ktor.http.cio.websocket.timeout
import io.ktor.jackson.jackson
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.fixiki.mogame_server.core.Game
import ru.fixiki.mogame_server.core.GameImpl
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
}

