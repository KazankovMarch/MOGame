package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.users.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.users.UserUpdate
import ru.fixiki.mogame_server.testing.FileUtils.fullResourcePath
import ru.fixiki.mogame_server.testing.handleUsersWebSocketConversation
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import java.util.*
import java.util.concurrent.CountDownLatch

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
        val name = "same name"
        handleUsersWebSocketConversation(name, User.Role.PLAYER) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
        Thread.sleep(1000)
        handleUsersWebSocketConversation(name, User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
            secondPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
    }


    @Test
    fun `registration WHEN game-lead role is released THEN success response`(): Unit = withDefaultTestApplication {
        handleUsersWebSocketConversation("Dinara", User.Role.GAME_LEAD) { firstPlayerIncoming, _ ->
            firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
        Thread.sleep(1000)
        handleUsersWebSocketConversation("Andrey", User.Role.GAME_LEAD) { secondPlayerIncoming, _ ->
            secondPlayerIncoming.receiveT<RegistrationResponse.Success>()
        }
    }

    @Test
    fun `registration WHEN user reconnected with valid token THEN response is the same token`(): Unit =
        withDefaultTestApplication {
            var token: String? = null
            handleUsersWebSocketConversation("Andrey", User.Role.PLAYER) { incoming, _ ->
                incoming.receiveT<RegistrationResponse.Success>().token
                handleUsersWebSocketConversation("Dinara", User.Role.GAME_LEAD) { incoming, _ ->
                    token = incoming.receiveT<RegistrationResponse.Success>().token
                }
                incoming.receiveT<UserUpdate.Joined>()
                incoming.receiveT<UserUpdate.Joined>()
                incoming.receiveT<UserUpdate.Left>()
            }
            handleUsersWebSocketConversation("Dinara_228", User.Role.GAME_LEAD, token) { firstPlayerIncoming, _ ->
                val response = firstPlayerIncoming.receiveT<RegistrationResponse.Success>()
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
                        val joined = objectMapper.readValue(receive.data) as UserUpdate.Joined
                        println(joined)
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
                val receiveT = incoming.receiveT<UserUpdate.Joined>()
                println("A1 $receiveT")
                val receiveT1 = incoming.receiveT<UserUpdate.Joined>()
                println("A1 $receiveT1")
                val userLeftUpdate = incoming.receiveT<UserUpdate.Left>()
                assertEquals(userLeftNickname, userLeftUpdate.leftUserNickname)
            }
        }
        handleUsersWebSocketConversation(userLeftNickname, User.Role.PLAYER) { incoming, _ ->
            incoming.receiveT<RegistrationResponse.Success>()
            val receiveT = incoming.receiveT<UserUpdate.Joined>()
            println("B1 $receiveT")
            val receiveT1 = incoming.receiveT<UserUpdate.Joined>()
            println("B2 $receiveT1")
        }

        runBlocking {
            firstUserHandling.await()
        }
    }
}