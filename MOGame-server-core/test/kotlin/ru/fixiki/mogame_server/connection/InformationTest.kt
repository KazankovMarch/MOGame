package ru.fixiki.mogame_server.connection

import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.fixiki.mogame_server.testing.FileUtils
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class InformationTest {

    private fun <R> withDefaultTestApplication(test: TestApplicationEngine.() -> R): R =
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, FileUtils.fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            users()
            information()
        }, test)



}