package ru.fixiki.mogame_server.model

data class User(
    val info: Info,
    var score: Int
) {

    enum class Role(value: String) {
        PLAYER("player"),
        GAME_LEAD("game_lead")
        //TODO SPECTATOR("spectator")
    }

    data class Info(
        val nickname: String,
        val role: Role,
        val avatar: ByteArray?
    )

}