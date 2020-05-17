package ru.fixiki.mogame_server.unpacking

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ru.fixiki.mogame_server.model.gamepackage.QuestionType

class QuestionTypeDeserializer : StdDeserializer<QuestionType>(QuestionType::class.java) {

    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): QuestionType {
        parser.nextFieldName()

        return when (val typeName = parser.nextTextValue()) {

            "auction" -> parseAuction(parser)
            "sponsored" -> parseSponsored(parser)
            "cat" -> parseCat(parser)
            "bagcat" -> parseBagCat(parser)
            "Другой" -> parseOther(parser)

            else -> throw IllegalArgumentException("unknown question type: $typeName")
        }
    }

    private fun parseAuction(parser: JsonParser): QuestionType.Auction {
        parser.nextFieldName()
        return QuestionType.Auction()
    }

    private fun parseSponsored(parser: JsonParser): QuestionType.Sponsored {
        parser.nextFieldName()
        return QuestionType.Sponsored()
    }

    private fun parseCat(parser: JsonParser): QuestionType.Cat {
        val paramMap = HashMap<String, String>()
        repeat(2) {
            parseParam(parser).apply {
                paramMap[first] = second
            }
        }
        parser.nextFieldName()
        return QuestionType.Cat(
            theme = paramMap["theme"]!!,
            cost = paramMap["cost"]!!.toInt()
        )
    }

    private fun parseBagCat(parser: JsonParser): QuestionType.BagCat {
        val paramMap = HashMap<String, String>()
        repeat(4) {
            parseParam(parser).apply {
                paramMap[first] = second
            }
        }
        parser.nextFieldName()
        return QuestionType.BagCat(
            theme = paramMap["theme"]!!,
            cost = paramMap["cost"]!!.toInt(),
            self = paramMap["self"]!!.toBoolean(),
            knows = QuestionType.BagCat.Knows.valueOf(paramMap["knows"]!!.toUpperCase())
        )
    }

    private fun parseOther(parser: JsonParser): QuestionType.Other {
        parser.nextFieldName()
        return QuestionType.Other()
    }

    private fun parseParam(parser: JsonParser): Pair<String, String> {
        parser.nextFieldName()
        parser.nextFieldName()
        parser.nextTextValue()
        val name = parser.nextTextValue()
        parser.nextTextValue()
        val value = parser.nextTextValue()
        parser.nextFieldName()
        return Pair(name, value)
    }
}
