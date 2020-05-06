package ru.fixiki.mogame_server.core

import kotlinx.coroutines.channels.ReceiveChannel
import ru.fixiki.mogame_server.model.dto.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate

interface UsersHolder {

    suspend fun tryRegisterUser(request: RegistrationRequest, token: String?): RegistrationResponse

    fun tokenIsValid(token: String): Boolean

    suspend fun subscribeToUsersInfo(token: String): ReceiveChannel<UserUpdate>

    suspend fun disconnectUser(token: String)

    fun nicknameIsBusy(nickname: String): Boolean

    fun gameLeadRoleIsBusy(): Boolean
}