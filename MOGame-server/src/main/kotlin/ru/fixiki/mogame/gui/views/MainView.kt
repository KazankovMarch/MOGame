package ru.fixiki.mogame.gui.views

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import ru.fixiki.mogame.connection.GameServer
import ru.fixiki.mogame.gui.Styles
import tornadofx.*
import java.io.File

class MainView : View("MOGame Server") {
    private var gamePackageFile = SimpleObjectProperty<File>()
    private var portProperty = SimpleStringProperty()
    private var startButton: Button by singleAssign()
    private var stopButton: Button by singleAssign()
    private var label: Label by singleAssign()

    override val root = borderpane {
        addClass(Styles.welcomeScreen)
        top {
            stackpane {
                label = label(title).addClass(Styles.heading)
            }
        }
        center {
            addClass(Styles.content)
            vbox {
                hbox {
                    label("Port for server")
                    textfield {
                        filterInput { it.controlNewText.isInt() }
                    }.textProperty().bindBidirectional(portProperty)
                }
                hbox {
                    button {
                        text = "Select Game Package"
                        action {
                            gamePackageFile.set(chooseFile("", arrayOf()).firstOrNull())
                        }
                    }
                    startButton = button {
                        text = "Start"
                        action {
                            if (label.text != "MOGame Server") return@action

                            val errors = validateData()
                            if (errors.isEmpty()) {
                                label.text = "Starting..."
                                runAsyncWithProgress {
                                    GameServer.start(gamePackageFile.get(), portProperty.get().toInt())
                                }.ui {
                                    isDisable = true
                                    stopButton.isDisable = false
                                    label.text = "MOGame Server (Launched)"
                                }
                            } else {
                                alert(
                                        type = Alert.AlertType.WARNING,
                                        header = "Can't start server",
                                        content = errors.joinToString()
                                )
                            }
                        }
                    }
                    stopButton = button {
                        text = "Stop"
                        isDisable = true
                        action {
                            label.text = "Stopping..."
                            isDisable = true
                            runAsync {
                                GameServer.stop()
                            }.ui {
                                label.text = "MOGame Server"
                                startButton.isDisable = false
                            }
                        }
                    }
                }

            }
        }
    }

    private fun validateData(): List<String> {
        val errors = ArrayList<String>()

        val gamePackage = gamePackageFile.get()
        if (gamePackage == null) {
            errors += "Game package not selected"
        } else {
            if (!gamePackage.name.endsWith(".siq")) {
                errors += "Game package does not end with .siq"
            }
        }

        if (portProperty.get() == null) {
            errors += "Server port not specified"
        }

        return errors
    }

    override fun onUndock() {
        runAsync {
            GameServer.stop()
        }
    }
}