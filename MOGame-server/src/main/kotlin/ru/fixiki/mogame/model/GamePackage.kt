package ru.fixiki.mogame.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import ru.fixiki.mogame.unpacking.MediaContentMultiResolver
import java.io.File

data class GamePackage(
        val name: String,
        val publisher: String?,
        val date: String,
        val difficulty: String,
        var logo: File? = null,
        val info: Info,
        val rounds: List<Round>
) {
    data class Info(
            val authors: List<String>?,
            val sources: List<String>?,
            val comments: String?
    )

    @JacksonXmlProperty(
            isAttribute = true,
            localName = "logo"
    )
    fun setLogo(name: String) {
        logo = MediaContentMultiResolver.getImageFileFromLocal(name)
    }
}

