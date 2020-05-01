package ru.fixiki.mogame.unpacking

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.fixiki.mogame.unpacking.archive.ArchivePackageLoader
import java.io.File

const val VALID_TEST_PACKAGE = "Package.siq"
const val PACKAGE_WITH_CONTENT_XML = "PackageWithContentXml.siq"
const val PACKAGE_WITH_LOGO = "PackageWithLogo.siq"

class ArchiveGamePackageLoaderTest {

    private val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()

    @Test
    fun `WHEN package is valid THEN PackageInfo is read ok`() {
        val gamePackage = loadPackage(PACKAGE_WITH_CONTENT_XML)
        with(gamePackage.info) {
            assertEquals("Праздник к нам приходит!", this.comments)
            assertEquals(listOf("vk.com/paladin_denfol"), this.sources)
            assertEquals(listOf("Paladin Denfol"), this.authors)
        }
    }

    @Test
    fun `WHEN logo exists THEN logo loaded ok`() {
        val gamePackage = loadPackage(PACKAGE_WITH_LOGO)
        assertNotNull(gamePackage.logo)
    }

    private fun loadPackage(path: String) =
            ArchivePackageLoader.loadPackage(File(classLoader.getResource(path)!!.file))

}