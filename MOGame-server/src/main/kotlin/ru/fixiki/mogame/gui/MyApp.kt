package ru.fixiki.mogame.gui

import javafx.application.Application
import ru.fixiki.mogame.gui.views.MainView
import tornadofx.App

class MyApp: App(MainView::class, Styles::class)

/**
 * The main method is needed to support the mvn jfx:run goal.
 */
fun main(args: Array<String>) {
    Application.launch(MyApp::class.java, *args)
}
