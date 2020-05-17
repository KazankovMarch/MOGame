package ru.fixiki.mogame_server.unpacking


import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import ru.fixiki.mogame_server.connection.objectMapper
import ru.fixiki.mogame_server.model.gamepackage.*
import ru.fixiki.mogame_server.testing.FileUtils.loadTestPackage

const val PACKAGE_WITH_CONTENT_XML = "PackageWithContentXml"
const val PACKAGE_WITH_LOGO = "PackageWithLogo"
const val TOTAL_PACKAGE = "TOTAL_PACKAGE"

class GamePackageLoaderTest {

    @Test
    fun `WHEN package is valid THEN PackageInfo is read ok`() {
        val gamePackage = loadTestPackage(PACKAGE_WITH_CONTENT_XML)
        with(gamePackage.info) {
            assertEquals("Праздник к нам приходит!", this.comments)
            assertEquals(listOf("vk.com/paladin_denfol"), this.sources)
            assertEquals(listOf("Paladin Denfol"), this.authors)
        }
    }

    @Test
    fun `WHEN logo exists THEN logo loaded ok`() {
        val gamePackage = loadTestPackage(PACKAGE_WITH_LOGO)
        assertNotNull(gamePackage.logo)
    }

    @Test
    fun `WHEN package is valid THEN atoms loaded`() {
        val loadedPackage = loadTestPackage(TOTAL_PACKAGE)
        val expectedPackage = GamePackage(
            name = TOTAL_PACKAGE,
            publisher = "Тестовый издатель",
            date = "16.05.2020",
            difficulty = "5",
            logo = "@TEST_IMAGE.png",
            tags = listOf("тестовая тематика"),
            info = Info(
                authors = listOf("ANDREY"),
                sources = listOf("тестовый источник", "ти2"),
                comments = "тестовый комментарий"
            ),
            rounds = listOf(
                Round(
                    name = "тест-рануд1",
                    info = Info(
                        authors = listOf("теставтор2", "теставтрор"),
                        sources = listOf("источник2", "источник"),
                        comments = "комент1"
                    ),
                    themes = listOf(
                        Theme(
                            name = "тестовая тема 1",
                            info = Info(
                                authors = listOf("автор1", "автор2"),
                                sources = listOf("источник1", "источник2"),
                                comments = "комментарий"
                            ),
                            questions = listOf(
                                uberQuestion(
                                    price = 100,
                                    firstText = "вопрос1"
                                ),
                                uberQuestion(
                                    price = 200,
                                    firstText = "вопрос2",
                                    typeParams = emptyList()
                                ),
                                uberQuestion(
                                    price = 300,
                                    firstText = "вопрос3",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "555"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 400,
                                    firstText = "вопрос4",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема1"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "111"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.SELF
                                            value = "true"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.KNOWS
                                            value = "before"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос5",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема2"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "222"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.SELF
                                            value = "true"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.KNOWS
                                            value = "after"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос6",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема3"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "333"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.SELF
                                            value = "true"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.KNOWS
                                            value = "never"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос7",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема4"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "444"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.SELF
                                            value = "false"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.KNOWS
                                            value = "before"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос8",
                                    typeParams = listOf(
                                        Param().apply {
                                            name = QuestionParameterType.THEME
                                            value = "тема5"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.COST
                                            value = "555"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.SELF
                                            value = "false"
                                        },
                                        Param().apply {
                                            name = QuestionParameterType.KNOWS
                                            value = "after"
                                        }
                                    )
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос9",
                                    typeParams = emptyList()
                                ),
                                uberQuestion(
                                    price = 500,
                                    firstText = "вопрос10",
                                    typeParams = emptyList()
                                )
                            )
                        )
                    )
                ),
                Round(
                    name = "тест-рануд2",
                    type = "final",
                    info = Info(
                        authors = listOf("теставтор2", "теставтрор"),
                        sources = listOf("источник2", "источник"),
                        comments = "комент1"
                    ),
                    themes = listOf(
                        Theme(
                            name = "тестовая тема 1",
                            info = Info(
                                authors = listOf("автор1", "автор2"),
                                sources = listOf("источник1", "источник2"),
                                comments = "комментарий"
                            ),
                            questions = listOf(
                                Question(
                                    price = 555,
                                    info = Info(
                                        authors = listOf(
                                            "автор вопроса",
                                            "автор вопроса2"
                                        ),
                                        sources = listOf(
                                            "источник вопроса",
                                            "ив2"
                                        ),
                                        comments = "комментарий к вопросу"
                                    ),
                                    scenario = listOf(
                                        Atom().apply {
                                            type = ContentType.TEXT
                                            value = "текст 1"
                                        },
                                        Atom().apply {
                                            type = ContentType.TEXT
                                            value = "текст2"
                                        }
                                    ),
                                    right = listOf(
                                        "правильный ответ",
                                        "правильный ответ2"
                                    ),
                                    wrong = listOf(
                                        "неправильный ответ",
                                        "неправильный ответ 2"
                                    )
                                )
                            )
                        ),
                        Theme(
                            name = "тестовая тема 2",
                            questions = listOf(
                                Question(
                                    price = 100,
                                    scenario = listOf(
                                        Atom().apply {
                                            type = ContentType.TEXT
                                            value = "текст"
                                        }
                                    ),
                                    right = listOf(
                                        "правильный ответ"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        assertEquals(
            """
                expected:
                ${objectMapper.writeValueAsString(expectedPackage)}
                loaded:
                ${objectMapper.writeValueAsString(loadedPackage)}
                
                
                
            """,
            expectedPackage,
            loadedPackage
        )
    }

    private fun uberQuestion(
        firstText: String,
        typeParams: List<Param>? = null,
        price: Int
    ) = Question(
        price = price,
        info = Info(
            authors = listOf("автор вопроса", "автор вопроса2"),
            sources = listOf("источник вопроса", "ив2"),
            comments = "комментарий к вопросу"
        ),
        scenario = listOf(
            Atom().apply {
                type = ContentType.TEXT
                value = firstText
            },
            Atom().apply {
                type = ContentType.IMAGE
                value = "@TEST_IMAGE.png"
            },
            Atom().apply {
                type = ContentType.SAY
                value = "Устный текст"
            },
            Atom().apply {
                type = ContentType.TEXT
                value = "текст"
            },
            Atom().apply {
                type = ContentType.VIDEO
                value = "@ezgif-6-ad18a845a591.mp4"
            },
            Atom().apply {
                type = ContentType.MARKER
                value = null
            },
            Atom().apply {
                type = ContentType.VOICE
                value = "@426888__thisusernameis__beep4.wav"
            },
            Atom().apply {
                type = ContentType.TEXT
                value = "текст ответа"
            },
            Atom().apply {
                type = ContentType.IMAGE
                value = "https://sun9-37.userapi.com/GXRpRh9eDG2zCcoyg7qyxPLbKZBZ9OF-mN5fyA/euKcqP5h3dY.jpg"
            }
        ),
        right = listOf(
            "правильный ответ",
            "правильный ответ2"
        ),
        wrong = listOf(
            "неправильный ответ",
            "неправильный ответ 2"
        ),
        typeParams = typeParams
    )
}