package ru.fixiki.mogame_server.model.dto.users

import ru.fixiki.mogame_server.model.User

sealed class UserUpdate {

    data class Joined(
        val user: User
    ) : UserUpdate()

    data class Changed(
        val user: User
    ) : UserUpdate()

    data class Left(
        val nickname: String
    ) : UserUpdate()
}