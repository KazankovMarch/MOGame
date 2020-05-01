package ru.fixiki.mogame.unpacking.archive

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.fixiki.mogame.model.GamePackage
import ru.fixiki.mogame.unpacking.MediaContentMultiResolver
import ru.fixiki.mogame.unpacking.PackageLoader
import java.io.File
import java.util.zip.ZipFile

object ArchivePackageLoader : PackageLoader {
    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
    private val xmlMapper = XmlMapper()
            .registerKotlinModule()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun loadPackage(file: File): GamePackage {
        val destDirectory = getDestDirectory(file.nameWithoutExtension)
        unpackArchive(file, destDirectory)
        MediaContentMultiResolver.setCurrentResolver(ArchiveMediaContentResolver)
        ArchiveMediaContentResolver.gamePackageDirectory = destDirectory
        val contentXmlFile = File("${destDirectory.absolutePath}/content.xml")
        return xmlMapper.readValue(contentXmlFile)
    }

    private fun getDestDirectory(packageName: String): File {
        val packagesURL = classLoader.getResource("packages/.fantom")!!
        return File(packagesURL.file.replaceAfterLast("/", packageName)).apply {
            if (exists()) {
                delete()
            }
            mkdir()
        }
    }

    private fun unpackArchive(archive: File, destDirectory: File) {
        createSubDirectory(destDirectory, TEXTS)
        createSubDirectory(destDirectory, AUDIO)
        createSubDirectory(destDirectory, VIDEO)
        createSubDirectory(destDirectory, IMAGES)
        ZipFile(archive).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    File("${destDirectory.absolutePath}/${entry.name}").outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    private fun createSubDirectory(destDirectory: File, newDirectoryName: String) {
        File("${destDirectory.absolutePath}/$newDirectoryName").mkdir()
    }
}