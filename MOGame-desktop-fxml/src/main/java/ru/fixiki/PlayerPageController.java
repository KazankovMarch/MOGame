package ru.fixiki;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerPageController {
    //    public PlayerPageController()
//    {
//        userInfoInGame.setText(userInfo.nickname);
//    }
    private UserInfo userInfo;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button outSelected;

    @FXML
    private ImageView userImageInGame;

    @FXML
    private Label userInfoInGame;

    @FXML
    void outSelected(ActionEvent event) throws IOException {
        App.setRoot("firstpage");
    }

    @FXML
    void initialize() {
        //  userImageInGame.setImage(new Image(userInfo.userImage));

        assert outSelected != null : "fx:id=\"outSelected\" was not injected: check your FXML file '2.fxml'.";

    }
}

