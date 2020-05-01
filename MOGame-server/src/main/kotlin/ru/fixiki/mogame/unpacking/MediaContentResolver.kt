package ru.fixiki.mogame.unpacking

import java.io.File

interface MediaContentResolver {
    fun getImageFileFromLocal(imageName: String): File
}