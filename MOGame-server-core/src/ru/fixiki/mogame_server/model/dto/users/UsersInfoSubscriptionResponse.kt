package ru.fixiki.mogame_server.model.dto.users

import kotlinx.coroutines.channels.ReceiveChannel

data class UsersInfoSubscriptionResponse(
    val subscription: ReceiveChannel<UserUpdate>,
    val initValues: List<UserUpdate>
)