package ru.fixiki.mogame_server.model.gamepackage

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Round(
    val name: String?,
    @JacksonXmlProperty(isAttribute = true)
    val type: String? = null,
    val themes: List<Theme>,
    val info: Info?
)
