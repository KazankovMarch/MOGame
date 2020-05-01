package ru.fixiki;

import javafx.scene.image.ImageView;

public class UserInfo {
    public String nickname;
    public ImageView userImage;

    public UserInfo(String nickname, ImageView userImage) {
        this.nickname = nickname;
        this.userImage = userImage;
    }
}
