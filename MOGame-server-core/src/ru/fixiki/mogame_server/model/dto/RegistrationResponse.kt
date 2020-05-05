package ru.fixiki.mogame_server.model.dto

sealed class RegistrationResponse {
    data class Success(
        val token: String
    ) : RegistrationResponse()

    data class Failure(
        val reason: String
    ) : RegistrationResponse()

    companion object {
        fun busyNickname() = Failure("nickname is busy")
        fun gameLeadRoleBusy() = Failure("game lead role is busy")
    }
}