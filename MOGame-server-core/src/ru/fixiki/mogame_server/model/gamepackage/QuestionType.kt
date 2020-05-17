package ru.fixiki.mogame_server.model.gamepackage

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import ru.fixiki.mogame_server.unpacking.QuestionTypeDeserializer

@JsonDeserialize(using = QuestionTypeDeserializer::class)
sealed class QuestionType(
    val stringValue: String
) {


    class Usual : QuestionType("usual")

    class Auction : QuestionType("auction")

    class Sponsored : QuestionType("sponsored")

    class Other : QuestionType("other")

    data class Cat(
        val theme: String,
        val cost: Int
    ) : QuestionType("cat")

    data class BagCat(
        val theme: String,
        val cost: Int,
        val self: Boolean,
        val knows: Knows
    ) : QuestionType("bagcat") {
        enum class Knows {
            BEFORE, AFTER, NEVER
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuestionType) return false

        if (stringValue != other.stringValue) return false

        return true
    }

    override fun hashCode(): Int {
        return stringValue.hashCode()
    }
}