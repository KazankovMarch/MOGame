module ru.fixiki {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.fixiki to javafx.fxml;
    exports ru.fixiki;
}