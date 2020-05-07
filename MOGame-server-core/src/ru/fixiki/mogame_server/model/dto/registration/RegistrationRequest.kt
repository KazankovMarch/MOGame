package ru.fixiki.mogame_server.model.dto.registration

import ru.fixiki.mogame_server.model.User

data class RegistrationRequest(
    val nickname: String,
    val role: User.Role
)