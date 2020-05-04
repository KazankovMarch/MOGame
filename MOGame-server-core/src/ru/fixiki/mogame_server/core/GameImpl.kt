package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import ru.fixiki.mogame_server.model.gamepackage.GamePackage
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class GameImpl(gamePackage: GamePackage) : Game {
    override suspend fun tryRegisterUser(userInfo: User.Info): RegistrationResponse {
        return RegistrationResponse.Success(UUID.randomUUID())
    }

    override suspend fun isValidUuid(uuid: String): Boolean {
        return true
    }

    override fun newUserChangesQueue(uuid: String): BlockingQueue<UserUpdate> {
        return ArrayBlockingQueue(1)
    }

}
