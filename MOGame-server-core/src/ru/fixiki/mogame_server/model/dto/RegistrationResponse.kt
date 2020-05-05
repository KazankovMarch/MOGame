package ru.fixiki.mogame_server.model.dto

sealed class RegistrationResponse {
    data class Success(
        val token: String
    ) : RegistrationResponse()

    data class Failure(
        val reason: String
    ) : RegistrationResponse()
}