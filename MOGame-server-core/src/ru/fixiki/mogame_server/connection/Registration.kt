package ru.fixiki.mogame_server.connection

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.Cookie
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import ru.fixiki.mogame_server.model.dto.registration.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.registration.RegistrationResponse

/**
 * Module responsible for entering game and editing profile
 * */
@Suppress("unused")
fun Application.registration() {
    routing {
        post(REGISTRATION) {
            try {
                val request = call.receive<RegistrationRequest>()
                val response = game.tryRegisterUser(request, call.request.cookies[TOKEN_COOKIE_NAME])
                if (response is RegistrationResponse.Success) {
                    call.response.cookies.append(Cookie(name = TOKEN_COOKIE_NAME, value = response.token))
                }
                call.respond(response)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
        post(AVATAR) {

        }
    }
}