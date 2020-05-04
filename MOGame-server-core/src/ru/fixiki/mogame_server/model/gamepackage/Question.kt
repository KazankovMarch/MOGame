package ru.fixiki.mogame_server.model.gamepackage

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Question(
    @JacksonXmlProperty(isAttribute = true)
    val price: Int
)