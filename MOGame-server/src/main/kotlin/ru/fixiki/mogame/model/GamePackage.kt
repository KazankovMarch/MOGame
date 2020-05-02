package ru.fixiki.mogame.model

data class GamePackage(
        val name: String,
        val publisher: String?,
        val date: String,
        val difficulty: String,
        var logo: String?,
        val info: Info,
        val rounds: List<Round>
) {
    data class Info(
            val authors: List<String>?,
            val sources: List<String>?,
            val comments: String?
    )
}

