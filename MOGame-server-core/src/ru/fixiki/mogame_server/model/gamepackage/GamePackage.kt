package ru.fixiki.mogame_server.model.gamepackage

data class GamePackage(
    val name: String,
    val publisher: String?,
    val date: String,
    val difficulty: String,
    val logo: String?,
    val info: Info,
    val tags: List<String>?,
    val rounds: List<Round>
)

