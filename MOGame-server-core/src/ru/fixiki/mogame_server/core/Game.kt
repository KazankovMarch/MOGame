package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import java.util.concurrent.BlockingQueue

interface Game {
    suspend fun tryRegisterUser(userInfo: User.Info): RegistrationResponse
    suspend fun isValidToken(token: String): Boolean
    fun newUserChangesQueue(token: String): BlockingQueue<UserUpdate>

}