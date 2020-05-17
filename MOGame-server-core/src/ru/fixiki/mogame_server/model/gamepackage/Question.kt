package ru.fixiki.mogame_server.model.gamepackage

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Question(
    @JacksonXmlProperty(isAttribute = true)
    val price: Int,
    val info: Info? = null,
    val scenario: List<Atom>,
    val right: List<String>,
    val wrong: List<String>? = null,
    val type: QuestionType = QuestionType.Usual()
)