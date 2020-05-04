package ru.fixiki.mogame_server.model.dto

import java.util.*

sealed class RegistrationResponse {
    data class Success(
        val uuid: UUID
    ) : RegistrationResponse()

    data class Failure(
        val reason: String
    ) : RegistrationResponse()
}