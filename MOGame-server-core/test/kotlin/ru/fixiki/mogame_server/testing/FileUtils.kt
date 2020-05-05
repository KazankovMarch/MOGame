package ru.fixiki.mogame_server.testing

import ru.fixiki.mogame_server.unpacking.GamePackageLoader

object FileUtils {

    fun loadTestPackage(path: String) =
            GamePackageLoader.loadPackage(fullResourcePath(path))

    fun fullResourcePath(relativeResourcePath: String): String =
            javaClass.classLoader.getResource(relativeResourcePath)!!.file
}
