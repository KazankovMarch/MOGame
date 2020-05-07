package ru.fixiki.mogame_server.connection


/**
 * Clients must pass registration before entering game or reconnecting.
 * In next HTTP requests token cookie must exists.
 * In next WebSocket connections token must be first message from client.
 * Clients receive information about other clients through this socket
 * */
const val USERS = "/users"

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
 * Game lead starts the game through this entry point
 * */
const val START = "/start"

/**
 * Clients receive game table at the start of the round through this socket
 * */
const val ROUND_GAME_TABLE = "/round_table"

/**
 * Client selects category and cost of question through this entry point
 * */
const val QUESTION_SELECT = "/question/select"

/**
 * Clients receive selected question info through this socket
 * E.g. category, cost, type
 * */
const val CURRENT_QUESTION_INFO = "/question/info"

/**
 * Client makes choice who will answer the question
 * */
const val BAG_CAT_CHOICE = "/question/bag_cat_choice"

/**
 * Clients receive choice made in bag-cat question
 * */
const val SHOW_BAG_CAT_CHOICE = "/question/bag_cat_choice/show"

/**
 * Game says who should make a bet now
 * */
const val AUCTION_ORDER = "/question/action/player"

/**
 * Client makes bet.
 * */
const val AUCTION_BET = "/question/action/bet"

/**
 * Clients receive bet made in auction question
 * */
const val SHOW_AUCTION_BET = "/question/action/bet"

/**
 * Clients receive scenario of the question and signal to show it
 * (server sends a signal after all clients have successfully downloaded the scenario)
 * */
const val SCENARIO = "/question/scenario"

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