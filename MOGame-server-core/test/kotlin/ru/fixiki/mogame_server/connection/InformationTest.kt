package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.users.UserUpdate
import ru.fixiki.mogame_server.testing.FileUtils
import ru.fixiki.mogame_server.testing.registerAndGetToken
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import java.util.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class InformationTest {

    private fun <R> withDefaultTestApplication(test: TestApplicationEngine.() -> R): R =
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, FileUtils.fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            registration()
            information()
        }, test)

    @Test
    fun `users info WHEN users are registered THEN userSocket sends their info`(): Unit = withDefaultTestApplication {
        val names = setOf("Vasya", "Nastya", "Petr")
        val nameIterator = names.iterator()

        val token1 = registerAndGetToken(nameIterator.next(), User.Role.GAME_LEAD.toString())
        val token2 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())
        val token3 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())

        val tokens = setOf(token1, token2, token3)

        val futures = tokens.map { token ->
            async {
                handleWebSocketConversation(USERS) { incoming, outgoing ->
                    outgoing.send(Frame.Text(token))
                    val receivedUserNames = HashSet<String>(tokens.size)
                    repeat(tokens.size) {
                        val receivedData = incoming.receive().data
                        with(objectMapper.readValue(receivedData) as UserUpdate.Joined) {
                            Assert.assertEquals(user.score, 0)
                            receivedUserNames.add(user.nickname)
                        }
                    }
                    Assert.assertEquals(names, receivedUserNames)
                    while (!incoming.isEmpty) {
                        val receive = incoming.receive()
                        val joined = objectMapper.readValue(receive.data) as UserUpdate.Joined
                        println(joined)
                    }
                    Assert.assertTrue(incoming.isEmpty)
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
        val token1 = registerAndGetToken("Andrey", User.Role.GAME_LEAD.toString())
        val token2 = registerAndGetToken(userLeftNickname, User.Role.PLAYER.toString())

        val firstUserHandling = async {
            handleWebSocketConversation(USERS) { incoming, outgoing ->
                outgoing.send(Frame.Text(token1))
                incoming.receiveT() as UserUpdate.Joined
                incoming.receiveT() as UserUpdate.Joined
                val leftUpdate = incoming.receiveT() as UserUpdate.Left
                assertEquals(userLeftNickname, leftUpdate.nickname)
            }
        }

        handleWebSocketConversation(USERS) { _, outgoing ->
            outgoing.send(Frame.Text(token2))
            outgoing.send(Frame.Close())
        }

        runBlocking {
            firstUserHandling.await()
        }
    }

}