package ru.fixiki.mogame_server.model

data class User(
    val nickname: String,
    val role: Role,
    val avatar: ByteArray? = null,
    var score: Int = 0
) {

    enum class Role(value: String) {
        PLAYER("player"),
        GAME_LEAD("game_lead")
        //TODO SPECTATOR("spectator")
    }

}