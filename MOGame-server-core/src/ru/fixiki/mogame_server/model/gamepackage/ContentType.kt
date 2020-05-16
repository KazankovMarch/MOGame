package ru.fixiki.mogame_server.model.gamepackage

enum class ContentType(val stringValue: String) {
    TEXT("text"),
    IMAGE("image"),
    VIDEO("video"),
    VOICE("voice"),
    SAY("say"),
    MARKER("marker")
}