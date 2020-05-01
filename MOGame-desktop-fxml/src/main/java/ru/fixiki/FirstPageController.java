package ru.fixiki;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstPageController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button playerSelected;

    @FXML
    private Button masterSelected;

    @FXML
    private TextField setNickname;

    @FXML
    private ImageView userImage;

    @FXML
    private Button photoSelected;

    @FXML
    private TextField setServerAddress;

    @FXML
    private ProgressBar progressLoadingBar;

    @FXML
    public void masterSelected(ActionEvent event) throws IOException {
        App.setRoot("MasterPage");
    }

    @FXML
    public void photoSelected(ActionEvent event) {
        int k = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            URI uri = file.toURI(); // Преобразуем файл в URI.
            Image img = new Image(uri.toString());
            if (userImage.getImage() == null)
                progressLoadingBar.setProgress(progressLoadingBar.getProgress() + 0.33);
            userImage.setImage(img);
        }
    }

    @FXML
    void setNickname(ActionEvent event) {

    }

    @FXML
    void setServerAddress(ActionEvent event) {
    }

    @FXML
    public void playerSelected(ActionEvent event) throws IOException {
        App.setRoot("PlayerPage");
        //App.setUserInfo(new UserInfo(setNickname.toString(), userImage));
    }

    @FXML
    void initialize() {
        assert playerSelected != null : "fx:id=\"playerSelected\" was not injected: check your FXML file 'firstpage.fxml'.";
        assert masterSelected != null : "fx:id=\"masterSelected\" was not injected: check your FXML file 'firstpage.fxml'.";
        assert setNickname != null : "fx:id=\"Nickname\" was not injected: check your FXML file 'firstpage.fxml'.";
        assert photoSelected != null : "fx:id=\"photoSelected\" was not injected: check your FXML file 'firstpage.fxml'.";
        assert setServerAddress != null : "fx:id=\"ServerAddress\" was not injected: check your FXML file 'firstpage.fxml'.";
        assert progressLoadingBar != null : "fx:id=\"progressLoadingBar\" was not injected: check your FXML file 'firstpage.fxml'.";

    }
}



