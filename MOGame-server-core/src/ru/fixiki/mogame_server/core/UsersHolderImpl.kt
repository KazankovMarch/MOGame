package ru.fixiki.mogame_server.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.registration.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.registration.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.users.UserUpdate
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse
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
                return RegistrationResponse.Success(token)
            }

            return when {
                nicknameIsBusy(nickname) -> RegistrationResponse.busyNickname()
                gameLeadRoleIsBusy() -> RegistrationResponse.busyRole()
                else -> {
                    val newToken = newToken()
                    users[newToken] = User(nickname, role)
                    RegistrationResponse.Success(newToken)
                }
            }
        }
    }

    override suspend fun subscribeToUsersInfo(token: String): UsersInfoSubscriptionResponse =
        connectedUsersMutex.withLock {
            val subscription = usersBroadcastChannel.openSubscription()
            val initValues = connectedUsers.map { UserUpdate.Joined(users[it]!!) }
            connectedUsers.add(token)
            usersBroadcastChannel.send(UserUpdate.Joined(users[token]!!))
            return UsersInfoSubscriptionResponse(subscription, initValues)
        }

    override suspend fun disconnectUser(token: String) {
        if (!connectedUsers.contains(token)) return

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