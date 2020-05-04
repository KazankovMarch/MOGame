package ru.fixiki.mogame_server.testing

import ru.fixiki.mogame_server.unpacking.GamePackageLoader

object FileUtils  {

    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

    fun loadTestPackage(path: String) =
        GamePackageLoader.loadPackage(fullResourcePath(path))

    fun fullResourcePath(relativeResourcePath: String) =
        classLoader.getResource(relativeResourcePath)!!.file


}
