package ru.fixiki.mogame_server.model.dto.users

sealed class RegistrationResponse {
    data class Success(
        val token: String
    ) : RegistrationResponse()

    data class Failure(
        val error: String
    ) : RegistrationResponse()

    companion object {
        fun busyNickname() = Failure("Nickname is busy")

        fun busyRole() = Failure("Role is busy")
    }
}