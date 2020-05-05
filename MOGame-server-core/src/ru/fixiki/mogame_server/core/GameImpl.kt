package ru.fixiki.mogame_server.core

import ru.fixiki.mogame_server.model.gamepackage.GamePackage

class GameImpl(
    private val gamePackage: GamePackage,
    private val usersHolder: UsersHolder = UsersHolderImpl()
) : Game, UsersHolder by usersHolder
