package ru.fixiki.mogame_server.model.dto

import ru.fixiki.mogame_server.model.User

data class RegistrationRequest(
    val userInfo: User.Info,
    val token: String?
)