package ru.fixiki.mogame.unpacking.archive

import ru.fixiki.mogame.unpacking.MediaContentResolver
import java.io.File

object ArchiveMediaContentResolver : MediaContentResolver {
    lateinit var gamePackageDirectory: File

    override fun getImageFileFromLocal(imageName: String): File {
        return File("${gamePackageDirectory.absolutePath}/Images/$imageName")
    }

}