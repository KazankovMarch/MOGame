package ru.fixiki.mogame_server.model.dto.registration

sealed class RegistrationResponse {
    data class Success(
        val token: String
    ) : RegistrationResponse()

    data class Failure(
        val reason: String
    ) : RegistrationResponse()

    companion object {
        fun busyNickname() =
            Failure("Nickname is busy")

        fun busyRole() =
            Failure("Role is busy")
    }
}