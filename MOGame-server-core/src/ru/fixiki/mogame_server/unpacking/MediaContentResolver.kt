package ru.fixiki.mogame_server.unpacking

import ru.fixiki.mogame_server.model.gamepackage.ContentType
import java.io.File

object MediaContentResolver {
    lateinit var gamePackageDirectory: File

    fun getImageFileFromLocal(imageName: String): File {
        return getMediaFileFromFolder(
            imageName,
            IMAGES
        )
    }

    fun getContent(name: String, type: ContentType): File {
        val folder = when (type) {
            ContentType.IMAGE -> IMAGES
            ContentType.AUDIO -> AUDIO
            ContentType.VIDEO -> VIDEO
            else -> throw IllegalArgumentException("Getting content with invalid type: $type")
        }
        return getMediaFileFromFolder(
            name,
            folder
        )
    }

    private fun getMediaFileFromFolder(fileName: String, folderName: String) =
        File("${gamePackageDirectory.absolutePath}/$folderName/$fileName")

}