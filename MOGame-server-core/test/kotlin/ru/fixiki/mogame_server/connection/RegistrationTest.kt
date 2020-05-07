package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.registration.RegistrationResponse
import ru.fixiki.mogame_server.testing.FileUtils.fullResourcePath
import ru.fixiki.mogame_server.testing.handleCorrectRegistrationRequest
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class RegistrationTest {

    @Test
    fun `registration WHEN request is correct THEN success response`() {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            registration()
        }) {
            with(handleCorrectRegistrationRequest("andrey", User.Role.PLAYER.toString())) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(response.content)
                objectMapper.readValue<RegistrationResponse.Success>(response.content!!)
            }
        }
    }

}