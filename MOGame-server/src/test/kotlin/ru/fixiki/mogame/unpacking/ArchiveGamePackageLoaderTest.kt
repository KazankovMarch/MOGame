package ru.fixiki.mogame.unpacking

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

const val VALID_TEST_PACKAGE = "TestPackage.siq"
const val VALID_TEST_PACKAGE_WITHOUT_MEDIA = "TestPackageWithoutMedia.siq"

class ArchiveGamePackageLoaderTest {

    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

    @Test
    fun `WHEN package is valid THEN PackageInfo is read ok`() {
        val gamePackage = ArchivePackageLoader.loadPackage(getGamePackage(VALID_TEST_PACKAGE_WITHOUT_MEDIA))
        with(gamePackage.info) {
            assertEquals("Праздник к нам приходит!", this.comments)
            assertEquals(listOf("vk.com/paladin_denfol"), this.sources)
            assertEquals(listOf("Paladin Denfol"), this.authors)
        }
    }

    private fun getGamePackage(path: String) = File(classLoader.getResource(path)!!.file)

}