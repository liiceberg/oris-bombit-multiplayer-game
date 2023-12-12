package ru.kpfu.itis.oris.gimaletdinova.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int position;
    private int img;
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return  username + " " + position;
    }
}
