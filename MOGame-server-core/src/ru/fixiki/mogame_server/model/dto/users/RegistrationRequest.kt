package ru.fixiki.mogame_server.model.dto.users

import ru.fixiki.mogame_server.model.User

data class RegistrationRequest(
    val nickname: String,
    val role: User.Role,
    val token: String?
)