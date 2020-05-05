package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Assert.*
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import ru.fixiki.mogame_server.testing.FileUtils.fullResourcePath
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import ru.fixiki.ru.fixiki.mogame_server.connection.GAME_FOLDER_PROPERTY
import ru.fixiki.ru.fixiki.mogame_server.connection.REGISTRATION_PATH
import ru.fixiki.ru.fixiki.mogame_server.connection.USERS_PATH
import ru.fixiki.ru.fixiki.mogame_server.connection.mainModule
import java.util.*

class ApplicationTest {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `registration WHEN request is correct THEN success response`() {
        withTestApplication ({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
        }) {
            with(handleCorrectRegistrationRequest("andrey", User.Role.PLAYER.toString())) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(response.content)
                objectMapper.readValue<RegistrationResponse.Success>(response.content!!)
            }
        }
    }

    @Test
    fun `users info WHEN users are registered THEN userSocket sends their info`() {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
        }) {
            val names = setOf("Vasya", "Nastya", "Petr")
            val nameIterator = names.iterator()

            val token1 = registerAndGetToken(nameIterator.next(), User.Role.GAME_LEAD.toString())
            val token2 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())
            val token3 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())

            val tokens = setOf(token1, token2, token3)
            for (token in tokens) {
                handleWebSocketConversation(USERS_PATH) { incoming, outgoing ->
                    outgoing.send(Frame.Text(token1.toString()))
                    val receivedUserNames = HashSet<String>(tokens.size)
                    repeat(tokens.size) {
                        with(objectMapper.readValue(incoming.receive().data) as UserUpdate.Joined) {
                            assertEquals(user.score, 0)
                            receivedUserNames.add(user.info.nickname)
                        }
                    }
                    assertEquals(names, receivedUserNames)
                    assertTrue(incoming.isEmpty)
                }
            }
        }
    }

    private fun TestApplicationEngine.registerAndGetToken(name: String, role: String) =
        (tryRegisterUser(name, role) as RegistrationResponse.Success).uuid

    private fun TestApplicationEngine.tryRegisterUser(name: String, role: String): RegistrationResponse {
        val content = handleCorrectRegistrationRequest(name, role).response.content!!
        return try {
            objectMapper.readValue<RegistrationResponse.Success>(content)
        } catch (e: JsonParseException) {
            objectMapper.readValue<RegistrationResponse.Failure>(content)
        } catch (e: JsonMappingException) {
            objectMapper.readValue<RegistrationResponse.Failure>(content)
        }
    }

    private fun TestApplicationEngine.handleCorrectRegistrationRequest(name: String, role: String) =
        handleRequest(HttpMethod.Post, REGISTRATION_PATH) {
            addHeader("content-type", "application/json")
            addHeader("Accept", "application/json")
            setBody(
                """{
                        "nickname": "$name",
                        "role": "$role"
                        }""".trimIndent()
            )
        }
}