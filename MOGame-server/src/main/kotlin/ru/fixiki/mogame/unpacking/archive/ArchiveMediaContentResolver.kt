package ru.fixiki.mogame.unpacking.archive

import ru.fixiki.mogame.model.ContentType
import ru.fixiki.mogame.unpacking.MediaContentResolver
import java.io.File

object ArchiveMediaContentResolver : MediaContentResolver {
    lateinit var gamePackageDirectory: File

    override fun getImageFileFromLocal(imageName: String): File {
        return getMediaFileFromFolder(imageName, IMAGES)
    }

    override fun getContent(name: String, type: ContentType): File {
        val folder = when (type) {
            ContentType.IMAGE -> IMAGES
            ContentType.AUDIO -> AUDIO
            ContentType.VIDEO -> VIDEO
            else -> throw IllegalArgumentException("Getting content with invalid type: $type")
        }
        return getMediaFileFromFolder(name, folder)
    }

    private fun getMediaFileFromFolder(fileName: String, folderName: String) =
            File("${gamePackageDirectory.absolutePath}/$folderName/$fileName")

}