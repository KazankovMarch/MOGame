package ru.fixiki.mogame_server.testing

import org.awaitility.core.ConditionFactory
import kotlin.reflect.KClass


fun <T : Any> ConditionFactory.untilNotThrows(expectedExceptions: KClass<T>, callback: () -> Unit) = until {
    try {
        callback.invoke()
        return@until true
    } catch (e: Exception) {
        if (e::class != expectedExceptions) {
            e.printStackTrace()
        }
        return@until false
    }
}
