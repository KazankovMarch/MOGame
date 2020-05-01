package ru.fixiki.mogame.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText


data class Atom(
        @JacksonXmlProperty(isAttribute = true)
        val type: ContentType,
        @JacksonXmlText
        val value: String
)

