package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.dto.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class UsersHolderImpl : UsersHolder {
    override fun tryRegisterUser(request: RegistrationRequest): RegistrationResponse {
        return RegistrationResponse.Success(UUID.randomUUID().toString())
    }

    override fun isTokenValid(token: String): Boolean {
        return true
    }

    override fun usersUpdatesQueue(token: String): BlockingQueue<UserUpdate> {
        return ArrayBlockingQueue(1)
    }
}