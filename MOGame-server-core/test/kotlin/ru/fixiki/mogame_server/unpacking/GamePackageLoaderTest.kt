package ru.fixiki.mogame_server.unpacking


import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import ru.fixiki.mogame_server.testing.FileUtils.loadTestPackage

const val PACKAGE_WITH_CONTENT_XML = "PackageWithContentXml"
const val PACKAGE_WITH_LOGO = "PackageWithLogo"

class GamePackageLoaderTest {

    @Test
    fun `WHEN package is valid THEN PackageInfo is read ok`() {
        val gamePackage = loadTestPackage(PACKAGE_WITH_CONTENT_XML)
        with(gamePackage.info) {
            assertEquals("Праздник к нам приходит!", this.comments)
            assertEquals(listOf("vk.com/paladin_denfol"), this.sources)
            assertEquals(listOf("Paladin Denfol"), this.authors)
        }
    }

    @Test
    fun `WHEN logo exists THEN logo loaded ok`() {
        val gamePackage = loadTestPackage(PACKAGE_WITH_LOGO)
        assertNotNull(gamePackage.logo)
    }


}