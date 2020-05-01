package ru.fixiki.mogame.unpacking

import ru.fixiki.mogame.model.GamePackage
import java.io.File

interface PackageLoader {
    fun loadPackage(file: File): GamePackage
}