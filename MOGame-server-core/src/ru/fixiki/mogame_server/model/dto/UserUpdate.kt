package ru.fixiki.mogame_server.model.dto

import ru.fixiki.mogame_server.model.User

sealed class UserUpdate {

    data class Joined(
        val users: User
    )

    data class Left(
        val nickname: String
    )
}