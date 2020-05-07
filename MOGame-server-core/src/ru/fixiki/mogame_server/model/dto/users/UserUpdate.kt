package ru.fixiki.mogame_server.model.dto.users

import ru.fixiki.mogame_server.model.User

sealed class UserUpdate {

    data class Joined(
        val joinedUser: User
    ) : UserUpdate()

    data class Changed(
        val changedUser: User
    ) : UserUpdate()

    data class Left(
        val leftUserNickname: String
    ) : UserUpdate()
}