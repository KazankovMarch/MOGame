package ru.fixiki.mogame_server.unpacking

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.fixiki.mogame_server.model.gamepackage.GamePackage
import java.io.File
import java.nio.file.Paths
import java.util.zip.ZipFile

object GamePackageLoader {
    private val xmlMapper = XmlMapper()
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun loadPackage(gameFolder: String): GamePackage {
        val contentXml = Paths.get(gameFolder, "content.xml").toFile()
        return xmlMapper.readValue(contentXml)
    }

}