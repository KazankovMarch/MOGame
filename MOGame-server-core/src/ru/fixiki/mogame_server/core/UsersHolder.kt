package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.dto.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import java.util.concurrent.BlockingQueue

interface UsersHolder {

    fun tryRegisterUser(request: RegistrationRequest): RegistrationResponse

    fun isTokenValid(token: String): Boolean

    fun usersUpdatesQueue(token: String): BlockingQueue<UserUpdate>

}