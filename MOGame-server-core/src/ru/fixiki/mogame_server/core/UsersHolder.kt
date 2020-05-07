package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.dto.users.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse

interface UsersHolder {

    suspend fun subscribeToUsersInfo(token: String): UsersInfoSubscriptionResponse

    suspend fun trySubscribeToUserInfoChannel(request: RegistrationRequest): UsersInfoSubscriptionResponse

    suspend fun disconnectUser(token: String)

    fun tokenIsValid(token: String): Boolean

    fun nicknameIsBusy(nickname: String): Boolean

    fun gameLeadRoleIsBusy(): Boolean
}