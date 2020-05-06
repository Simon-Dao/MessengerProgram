package client;

import javafx.scene.paint.Color;
import database.Friend;
import main.Main;

import java.util.ArrayList;

public class LocalUser {
    private String name;

    private String password;

    private String color;

    private int id;

    public ArrayList<Friend> friends = new ArrayList<>();

    /*
    public LocalUser() {
        friends.add(new Friend("bob","black"));
        friends.add(new Friend("jimmy","blue"));
    }
    */

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Color getColor() {

        Color userColor = null;

        switch (color) {
            case "blue":
                userColor = Color.BLUE;
                break;
            case "red":
                userColor = Color.RED;
                break;
            case "green":
                userColor = Color.GREEN;
                break;
            case "orange":
                userColor = Color.ORANGE;
                break;
            case "black":
                userColor = Color.BLACK;
                break;
        }

        return userColor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getColorInString() {
        return color;
    }

    public ArrayList<Friend> getFriendList() {
        return friends;
    }

    public void addFriend(String username, String color) {

        Main.app.contactsArea.addContact(username, color);

        Friend newFriend = new Friend(username, color);
        friends.add(newFriend);

        for(Friend f : friends)
        System.out.println(f.username);
    }

    //TODO add a method to remove friends
    public void removeFriend() {

    }
}
