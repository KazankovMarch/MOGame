package ru.fixiki.mogame_server.model.gamepackage

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class Question(
    @JacksonXmlProperty(isAttribute = true)
    val price: Int,
    val info: Info?,
    val scenario: List<Atom>,
    val right: List<String>,
    val wrong: List<String>?,
    @JacksonXmlProperty(localName = "type")
    val typeParams: List<Param>?
) {
    val questionType =
        if (typeParams == null) {
            Type.NORMAL
        } else when (typeParams.size) {
            0 -> Type.AUCTION
            2 -> Type.CAT
            4 -> Type.BAG_CAT
            else -> throw IllegalArgumentException("invalid type size: $typeParams")
        }

    enum class Type {
        AUCTION, CAT, BAG_CAT, NORMAL
    }
}