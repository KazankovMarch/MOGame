package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.dto.registration.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.registration.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse

interface UsersHolder {

    suspend fun tryRegisterUser(request: RegistrationRequest, token: String?): RegistrationResponse

    fun tokenIsValid(token: String): Boolean

    suspend fun subscribeToUsersInfo(token: String): UsersInfoSubscriptionResponse

    suspend fun disconnectUser(token: String)

    fun nicknameIsBusy(nickname: String): Boolean

    fun gameLeadRoleIsBusy(): Boolean
}