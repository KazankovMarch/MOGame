package ru.fixiki.mogame.unpacking

import ru.fixiki.mogame.model.ContentType
import java.io.File

interface MediaContentResolver {
    fun getImageFileFromLocal(imageName: String): File
    fun getContent(name: String, type: ContentType): File
}