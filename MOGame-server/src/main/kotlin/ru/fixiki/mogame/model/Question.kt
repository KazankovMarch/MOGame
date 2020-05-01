package ru.fixiki.mogame.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Question(
        @JacksonXmlProperty(isAttribute = true)
        val price: Int
)