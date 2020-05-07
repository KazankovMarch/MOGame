package ru.fixiki.mogame_server.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.fixiki.mogame_server.model.User
import ru.fixiki.mogame_server.model.dto.users.RegistrationRequest
import ru.fixiki.mogame_server.model.dto.users.RegistrationResponse
import ru.fixiki.mogame_server.model.dto.users.UserUpdate
import ru.fixiki.mogame_server.model.dto.users.UsersInfoSubscriptionResponse
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicReference

@ExperimentalCoroutinesApi
class UsersHolderImpl : UsersHolder {

    private val usersBroadcastChannel = BroadcastChannel<UserUpdate>(-2)

    private val gameLeadReference = AtomicReference<User>()

    private val users = ConcurrentHashMap<String, User>()

    private val usersMutex = Mutex()

    private val connectedUsers = ConcurrentSkipListSet<String>()

    private val connectedUsersMutex = Mutex()

    override suspend fun trySubscribeToUserInfoChannel(
        request: RegistrationRequest
    ): UsersInfoSubscriptionResponse = connectedUsersMutex.withLock {
        with(request) {
            return when {
                nicknameIsBusy(nickname) -> {
                    println(nickname)
                    UsersInfoSubscriptionResponse.busyNickname()
                }
                role == User.Role.GAME_LEAD && gameLeadRoleIsBusy() -> UsersInfoSubscriptionResponse.busyRole()
                token != null && tokenIsValid(token) -> {
                    val oldUserEntry = users[token]!!
                    users[token] = User(nickname, role, oldUserEntry.avatar, oldUserEntry.score)
                    subscribeToUsersInfo(token)
                }
                else -> {
                    val newToken = newToken()
                    users[newToken] = User(nickname, role)
                    subscribeToUsersInfo(newToken)
                }
            }
        }
    }

    override suspend fun subscribeToUsersInfo(token: String): UsersInfoSubscriptionResponse {
        if (users[token]!!.role == User.Role.GAME_LEAD) {
            gameLeadReference.set(users[token])
        }
        val initValues = connectedUsers.map { UserUpdate.Joined(users[it]!!) }
        connectedUsers.add(token)
        val subscription = usersBroadcastChannel.openSubscription()
        usersBroadcastChannel.send(UserUpdate.Joined(users[token]!!))
        return UsersInfoSubscriptionResponse.Success(subscription, initValues, RegistrationResponse.Success(token))
    }

    override suspend fun disconnectUser(token: String) {
        connectedUsersMutex.withLock {
            if (connectedUsers.contains(token)) {
                connectedUsers.remove(token)
                usersBroadcastChannel.send(UserUpdate.Left(users[token]!!.nickname))
                if (users[token]!!.role == User.Role.GAME_LEAD) {
                    gameLeadReference.set(null)
                }
            }
        }
    }

    override fun nicknameIsBusy(nickname: String): Boolean =
        users.any { connectedUsers.contains(it.key) && it.value.nickname == nickname }

    override fun gameLeadRoleIsBusy(): Boolean = gameLeadReference.get() != null

    override fun tokenIsValid(token: String): Boolean = users.containsKey(token)

    private fun newToken(): String = UUID.randomUUID().toString()
}