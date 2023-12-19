package ru.kpfu.itis.oris.gimaletdinova.model;

public class User {
    private String username;
    private int position;
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
    @Override
    public String toString() {
        return  username + " " + position;
    }
}
