package ru.fixiki.mogame.unpacking

import ru.fixiki.mogame.unpacking.archive.ArchiveMediaContentResolver

var currentResolver: MediaContentResolver = ArchiveMediaContentResolver

object MediaContentMultiResolver : MediaContentResolver by currentResolver {
    fun setCurrentResolver(resolver: MediaContentResolver) {
        currentResolver = resolver
    }
}