package ru.fixiki.mogame_server.connection


/**
 * Clients must pass registration before entering game or reconnecting.
 * In next HTTP requests token cookie must exists.
 * In next WebSocket connections token must be first message from client.
 * */
const val REGISTRATION = "/registration"

/**
 * Clients sends his avatars after registration
 * */
const val AVATAR = "/avatar"

/**
 * Clients receive common info about game package.
 * E.g. title, authors.
 * */
const val GAME_PACKAGE_INFO = "/package_info"

/**
 * Clients receive information about other clients through this socket
 * */
const val USERS = "/users"

/**
 * Game lead starts the game through this entry point
 * */
const val START = "/start"

/**
 * Clients receive current game table through this socket
 * */
const val GAME_TABLE = "/game_table"

/**
 * Client selects category and cost of question through this entry point
 * */
const val QUESTION_SELECT = "/question/select"

/**
 * Clients receive selected question type through this socket
 * E.g. auction, bag cat.
 * */
const val CURRENT_QUESTION_TYPE = "/question/type"

/**
 * Client makes choice who will answer the question.
 * Other users receive this choice
 * */
const val BAG_CAT_CHOICE = "/question/bag_cat_choice"

/**
 * Game says who should make a bet now
 * */
const val AUCTION_ORDER = "/question/action/player"

/**
 * Client makes bet.
 * Other users receive this bet.
 * */
const val AUCTION_BET = "/question/action/player"

/**
 * Clients receive scenario of the question
 * */
const val SCENARIO = "/question/scenario"

/**
 * After all clients have successfully downloaded the script,
 * server sends a signal to show it
 * */
const val SHOW_SCENARIO = "/question/scenario/show"

/**
 * Client says that he wants to answer the current question.
 * For usual questions
 * */
const val ANSWER_BUTTON = "/question/answer_button"

/**
 * Game lead receives correct answers.
 * If no one said correct answer, users receive correct answers too
 * */
const val CORRECT_ANSWERS = "/question/correct_answers"

/**
 * Game lead pauses game
 * */
const val PAUSE = "/pause"

/**
 * Game lead restores game
 * */
const val UNPAUSE = "/unpause"

/**
 * Game lead edits player's score
 * */
const val EDIT_SCORE = "/edit_score"