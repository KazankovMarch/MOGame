package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import ru.fixiki.mogame_server.model.gamepackage.GamePackage
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class GameImpl(private val gamePackage: GamePackage) : Game {


    override suspend fun tryRegisterUser(userInfo: User.Info): RegistrationResponse {
        return RegistrationResponse.Success(UUID.randomUUID().toString())
    }

    override suspend fun isValidToken(token: String): Boolean {
        return true
    }

    override fun newUserChangesQueue(token: String): BlockingQueue<UserUpdate> {
        return ArrayBlockingQueue(1)
    }

}
