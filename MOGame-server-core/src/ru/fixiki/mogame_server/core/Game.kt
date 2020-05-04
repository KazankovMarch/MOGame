package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import java.util.concurrent.BlockingQueue

interface Game {
    suspend fun tryRegisterUser(userInfo: User.Info): RegistrationResponse
    suspend fun isValidUuid(uuid: String): Boolean
    fun newUserChangesQueue(uuid: String): BlockingQueue<UserUpdate>

}