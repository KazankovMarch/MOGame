package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.users.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.users.UserUpdate
import ru.fixiki.mogame_server.testing.FileUtils.fullResourcePath
import ru.fixiki.mogame_server.testing.handleUsersWebSocketConversation
import ru.fixiki.mogame_server.testing.untilNotThrows
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class UsersTest {

    private fun <R> withDefaultTestApplication(test: TestApplicationEngine.() -> R): R =
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            users()
        }, test)

    @Test
    fun `registration WHEN request is correct THEN success response`(): Unit = withDefaultTestApplication {
        handleUsersWebSocketConversation("Andrey", User.Role.PLAYER) { incoming, _ ->
            incoming.receiveT<RegistrationResponse.Success>()
        }
    }

    @Test
    fun `registration WHEN nickname is busy THEN busyNickname response`(): Unit = withDefaultTestApplication {
        val name = "same name"
        handleUsersWebSocketConversation(name, User.Role.PLAYER) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
            handleUsersWebSocketConversation(name, User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
                val response: RegistrationResponse.Failure = secondPlayerIncoming.receiveT()
                assertEquals(RegistrationResponse.busyNickname(), response)
            }
        }
    }

    @Test
    fun `registration WHEN game-lead role is busy THEN roleIsBusy response`(): Unit = withDefaultTestApplication {
        handleUsersWebSocketConversation("Dinara", User.Role.GAME_LEAD) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
            handleUsersWebSocketConversation("Andrey", User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
                val response: RegistrationResponse.Failure = secondPlayerIncoming.receiveT()
                assertEquals(RegistrationResponse.busyRole(), response)
            }
        }
    }

    @Test
    fun `registration WHEN nickname is released THEN success response`(): Unit = withDefaultTestApplication {
        val popularNickname = "da Vinci"
        handleUsersWebSocketConversation(popularNickname, User.Role.PLAYER) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
        await().atMost(1, TimeUnit.SECONDS).untilNotThrows(JsonMappingException::class) {
            handleUsersWebSocketConversation(popularNickname, User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
                secondPlayerIncoming.receiveT<RegistrationResponse.Success>()
            }
        }
    }

    @Test
    fun `registration WHEN game-lead role is released THEN success response`(): Unit = withDefaultTestApplication {
        handleUsersWebSocketConversation("Dinara", User.Role.GAME_LEAD) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
        await().atMost(1, TimeUnit.SECONDS).untilNotThrows(JsonMappingException::class) {
            handleUsersWebSocketConversation("Andrey", User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
                secondPlayerIncoming.receiveT<RegistrationResponse.Success>()
            }
        }
    }

    @Test
    fun `registration WHEN user reconnected with valid token THEN response is the same token`(): Unit =
        withDefaultTestApplication {
            var token: String? = null
            handleUsersWebSocketConversation("Andrey", User.Role.PLAYER) { andreyIncoming, _ ->
                handleUsersWebSocketConversation("Dinara", User.Role.GAME_LEAD) { dinaraIncoming, _ ->
                    token = dinaraIncoming.receiveT<RegistrationResponse.Success>().token
                }
                andreyIncoming.receiveT<RegistrationResponse.Success>().token
                andreyIncoming.receiveT<UserUpdate.Joined>()
                andreyIncoming.receiveT<UserUpdate.Joined>()
                andreyIncoming.receiveT<UserUpdate.Left>()
            }
            handleUsersWebSocketConversation("Dinara_228", User.Role.GAME_LEAD, token) { dinaraIncoming, _ ->
                val response = dinaraIncoming.receiveT<RegistrationResponse.Success>()
                assertEquals(token, response.token)
            }
        }

    @Test
    fun `users info WHEN users are registered THEN userSocket sends their info`(): Unit = withDefaultTestApplication {
        val names = setOf("Vasya", "Nastya", "Petr")

        val latch = CountDownLatch(names.size)

        val futures = names.map { name ->
            async {
                handleUsersWebSocketConversation(name, User.Role.PLAYER) { incoming, _ ->
                    incoming.receiveT<RegistrationResponse.Success>()
                    val receivedUserNames = HashSet<String>(names.size)
                    repeat(names.size) {
                        val receivedUpdate: UserUpdate.Joined = incoming.receiveT()
                        with(receivedUpdate) {
                            assertEquals(0, joinedUser.score)
                            receivedUserNames.add(joinedUser.nickname)
                        }
                    }
                    assertEquals(names, receivedUserNames)
                    while (!incoming.isEmpty) {
                        val receive = incoming.receive()
                        objectMapper.readValue(receive.data) as UserUpdate.Joined
                    }
                    assertTrue(incoming.isEmpty)
                    latch.countDown()
                    latch.await()
                }
            }
        }
        runBlocking {
            futures.awaitAll()
        }
    }

    @Test
    fun `user info WHEN user leaves THEN userSocket send it's Left update`(): Unit = withDefaultTestApplication {
        val userLeftNickname = "Bob"

        val firstUserHandling = async {
            handleUsersWebSocketConversation("Robert", User.Role.GAME_LEAD) { incoming, _ ->
                incoming.receiveT<RegistrationResponse.Success>()
                incoming.receiveT<UserUpdate.Joined>()
                incoming.receiveT<UserUpdate.Joined>()
                val userLeftUpdate = incoming.receiveT<UserUpdate.Left>()
                assertEquals(userLeftNickname, userLeftUpdate.leftUserNickname)
            }
        }
        handleUsersWebSocketConversation(userLeftNickname, User.Role.PLAYER) { incoming, _ ->
            incoming.receiveT<RegistrationResponse.Success>()
            incoming.receiveT<UserUpdate.Joined>()
            incoming.receiveT<UserUpdate.Joined>()
        }

        runBlocking {
            firstUserHandling.await()
        }
    }
}