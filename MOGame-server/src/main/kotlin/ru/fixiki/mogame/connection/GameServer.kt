package ru.fixiki.mogame.connection


import ru.fixiki.mogame.unpacking.archive.ArchivePackageLoader
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object GameServer {
    private var running = AtomicBoolean(false)

    fun start(archive: File, port: Int) {
        running.set(true)

        val gamePackage = ArchivePackageLoader.loadPackage(archive)
    }

    fun stop() {
        ArchivePackageLoader.removePackagesFiles()
        running.set(false)
    }

}
