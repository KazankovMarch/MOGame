package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
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
import ru.fixiki.mogame_server.testing.registerAndGetToken
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class RegistrationTest {

    private fun <R> withDefaultTestApplication(test: TestApplicationEngine.() -> R): R =
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            registration()
        }, test)

    @Test
    fun `registration WHEN request is correct THEN success response`(): Unit = withDefaultTestApplication {
        with(handleCorrectRegistrationRequest("andrey", User.Role.PLAYER.toString())) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
            objectMapper.readValue<RegistrationResponse.Success>(response.content!!)
        }
    }

    @Test
    fun `registration WHEN nickname is busy THEN busyNickname response`(): Unit = withDefaultTestApplication {
        val name = "same name"
        registerAndGetToken(name, User.Role.PLAYER)
        with(handleCorrectRegistrationRequest(name, User.Role.GAME_LEAD.toString())) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
            val response = objectMapper.readValue<RegistrationResponse.Failure>(response.content!!)
            assertEquals(RegistrationResponse.busyNickname(), response)
        }
    }


    @Test
    fun `registration WHEN game-lead role is busy THEN roleIsBusy response`(): Unit = withDefaultTestApplication {
        registerAndGetToken("gl-1", User.Role.GAME_LEAD)
        with(handleCorrectRegistrationRequest("gl-2", User.Role.GAME_LEAD.toString())) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertNotNull(response.content)
            val response = objectMapper.readValue<RegistrationResponse.Failure>(response.content!!)
            assertEquals(RegistrationResponse.busyRole(), response)
        }
    }
}