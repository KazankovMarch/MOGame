package ru.fixiki.mogame_server.testing

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import ru.fixiki.mogame_server.connection.REGISTRATION
import ru.fixiki.mogame_server.connection.objectMapper
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.registration.RegistrationResponse

fun TestApplicationEngine.registerAndGetToken(name: String, role: User.Role) =
    (tryRegisterUser(name, role.toString()) as RegistrationResponse.Success).token

fun TestApplicationEngine.tryRegisterUser(name: String, role: String): RegistrationResponse {
    val content = handleCorrectRegistrationRequest(name, role).response.content!!
    return try {
        objectMapper.readValue<RegistrationResponse.Success>(content)
    } catch (e: JsonParseException) {
        objectMapper.readValue<RegistrationResponse.Failure>(content)
    } catch (e: JsonMappingException) {
        objectMapper.readValue<RegistrationResponse.Failure>(content)
    }
}

fun TestApplicationEngine.handleCorrectRegistrationRequest(name: String, role: String) =
    handleRequest(HttpMethod.Post, REGISTRATION) {
        addHeader("content-type", "application/json")
        addHeader("Accept", "application/json")
        setBody( //language=json
            """{
                    "nickname": "$name", 
                    "role": "$role"
                }""".trimIndent()
        )
    }