package ru.fixiki.mogame_server.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.UserUpdate
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicReference

@ExperimentalCoroutinesApi
class UsersHolderImpl : UsersHolder {

    private val usersBroadcastChannel = BroadcastChannel<UserUpdate>(-1)

    private val gameLeadReference = AtomicReference<User>()

    private val users = ConcurrentHashMap<String, User>()

    private val connectedUsers = ConcurrentSkipListSet<String>()

    private val connectedUsersMutex = Mutex()

    override suspend fun tryRegisterUser(
        request: RegistrationRequest,
        token: String?
    ): RegistrationResponse {
        with(request) {
            if (token != null && tokenIsValid(token)) {
                connectedUsersMutex.withLock {
                    connectedUsers.add(token)
                    usersBroadcastChannel.send(UserUpdate.Joined(users[token]!!))
                }
                return RegistrationResponse.Success(token)
            }

            return when {
                nicknameIsBusy(nickname) -> RegistrationResponse.busyNickname()
                gameLeadRoleIsBusy() -> RegistrationResponse.busyRole()
                else -> {
                    val newToken = newToken()
                    connectedUsersMutex.withLock {
                        users[newToken] = User(nickname, role)
                        connectedUsers.add(newToken)
                        connectedUsers.add(newToken)
                    }
                    RegistrationResponse.Success(newToken)
                }
            }
        }
    }

    //    TODO refactor
    override suspend fun subscribeToUsersInfo(token: String): ReceiveChannel<UserUpdate> {
        val withLock = connectedUsersMutex.withLock {
            return@withLock Pair(
                first = usersBroadcastChannel.openSubscription(),
                second = connectedUsers.map { users[it]!! })
        }
        return GlobalScope.produce {
            withLock.second.forEach { send(UserUpdate.Joined(it)) }
            while (!isClosedForSend) {
                send(withLock.first.receive())
            }
            withLock.first.cancel()
        }
    }

    override suspend fun disconnectUser(token: String) {
        connectedUsersMutex.withLock {
            connectedUsers.remove(token)
            usersBroadcastChannel.send(UserUpdate.Left(users[token]!!.nickname))
        }
    }

    override fun nicknameIsBusy(nickname: String): Boolean = users.values.any { it.nickname == nickname }

    override fun gameLeadRoleIsBusy(): Boolean = gameLeadReference.get() != null

    override fun tokenIsValid(token: String): Boolean = users.containsKey(token)

    private fun newToken(): String = UUID.randomUUID().toString()
}