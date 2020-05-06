package ru.fixiki.mogame_server.connection

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.config.MapApplicationConfig
import io.ktor.http.cio.websocket.Frame
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.UserUpdate
import ru.fixiki.mogame_server.testing.FileUtils
import ru.fixiki.mogame_server.testing.registerAndGetToken
import ru.fixiki.mogame_server.unpacking.PACKAGE_WITH_CONTENT_XML
import java.util.*

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
internal class InformationTest {

    @Test
    fun `users info WHEN users are registered THEN userSocket sends their info`() {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put(GAME_FOLDER_PROPERTY, FileUtils.fullResourcePath(PACKAGE_WITH_CONTENT_XML))
            }
            mainModule(testing = true)
            registration()
            information()
        }) {
            val names = setOf("Vasya", "Nastya", "Petr")
            val nameIterator = names.iterator()

            val token1 = registerAndGetToken(nameIterator.next(), User.Role.GAME_LEAD.toString())
            val token2 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())
            val token3 = registerAndGetToken(nameIterator.next(), User.Role.PLAYER.toString())

            val tokens = setOf(token1, token2, token3)
            for (token in tokens) {
                handleWebSocketConversation(USERS) { incoming, outgoing ->
                    outgoing.send(Frame.Text(token1))
                    val receivedUserNames = HashSet<String>(tokens.size)
                    repeat(tokens.size) {
                        val receivedData = incoming.receive().data
                        with(objectMapper.readValue(receivedData) as UserUpdate.Joined) {
                            Assert.assertEquals(user.score, 0)
                            receivedUserNames.add(user.nickname)
                        }
                    }
                    Assert.assertEquals(names, receivedUserNames)
                    Assert.assertTrue(incoming.isEmpty)
                }
            }
        }
    }

}