package ru.fixiki.mogame_server.model.dto.users

import kotlinx.coroutines.channels.ReceiveChannel

sealed class UsersInfoSubscriptionResponse {
    data class Success(
        val subscription: ReceiveChannel<UserUpdate>,
        val initValues: List<UserUpdate>,
        val successResponse: RegistrationResponse.Success
    ) : UsersInfoSubscriptionResponse()

    data class Failure(
        val failureResponse: RegistrationResponse.Failure
    ) : UsersInfoSubscriptionResponse()

    companion object {
        fun busyNickname() = Failure(RegistrationResponse.busyNickname())

        fun busyRole() = Failure(RegistrationResponse.busyRole())
    }
}