package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Assert.*
import org.junit.Test
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.testing.FileUtils.fullResourcePath
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import ru.fixiki.ru.fixiki.mogame_server.connection.GAME_FOLDER_PROPERTY
import ru.fixiki.ru.fixiki.mogame_server.connection.REGISTRATION_PATH
import ru.fixiki.ru.fixiki.mogame_server.connection.mainModule

class ApplicationTest {

    val objectMapper = jacksonObjectMapper()

    @Test
    fun `sdfs dfs`() {
        withTestApplication ({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
        }) {
            with(handleRequest(HttpMethod.Post, REGISTRATION_PATH) {
                addHeader("content-type", "application/json")
                addHeader("Accept", "application/json")
                setBody("""{
                        "nickname": "Andrey",
                        "role": "PLAYER"
                        }""".trimIndent())
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertNotNull(response.content)
                objectMapper.readValue<RegistrationResponse.Success>(response.content!!)
            }
        }
    }

}