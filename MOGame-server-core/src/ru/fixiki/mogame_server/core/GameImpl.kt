package ru.fixiki.mogame_server.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.fixiki.mogame_server.model.gamepackage.GamePackage

@ExperimentalCoroutinesApi
class GameImpl(
    private val gamePackage: GamePackage,
    private val usersHolder: UsersHolder = UsersHolderImpl()
) : Game, UsersHolder by usersHolder
