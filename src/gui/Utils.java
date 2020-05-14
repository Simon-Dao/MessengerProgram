package gui;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import client.Client;
import client.LocalUser;
import main.Main;

import java.io.IOException;
import java.net.Socket;

public class Utils {

    /**
     * sets a limit to the number of characters in the given textfield
     * @param field is the textfield that will be edited
     * @param length the number of char allowed
     */
    public void maxCharLength(TextField field, int length) {

        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                //is the text is not greater than the max and is not empty
                if (field.getText().length() > length) {
                    String s = field.getText().substring(0, length);
                    field.setText(s);
                }
            }
        });
    }

    /**
     * sends a string to the server
     * @param username
     * @param color
     * @param message
     */
    public void sendMessage(String username, String color, String message) {
        Main.client.out.println("!name!" + username + " !color!" + color + " !message!" + message);
        Main.client.out.flush();
    }

    public void sendMessage(String message) {
        Main.client.out.println(message);
        Main.client.out.flush();
    }

    /**
     * sends a login request to the server
     * @param username
     * @param password
     */
    public void sendUserData(String username, String password) {
        Main.client.out.println("!userinfo!" + username + ";" + password);
    }

    /**
     * sends a new account request to the server
     * @param username
     * @param password
     * @param color
     */
    public void sendNewAccountForm(String username, String password, String color) {
        Main.client.out.println("!newuser!" + username + " " + password + " " + color);
    }

    /**
     * checks if the username is taken in the database
     * @param name
     */
    public void checkIfNameTaken(String name) {
        Main.client.out.println("!checkDupes!" + name);
    }

    /**
     * checks if the server is alive
     * @param IP
     * @param PORT
     * @return
     */
    public boolean serverIsAlive(String IP, int PORT) {

        try (Socket s = new Socket(IP, PORT)) {
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * sends a
     * @param username
     * @param password
     * @param profile
     */
    public void createUser(String username, String password, String profile) {

        if (!username.isEmpty() && !password.isEmpty()) {

            changeScene(Main.messengerWindow);

            sendNewAccountForm(username, password, profile);
        }
    }

    public void changeScene(Scene scene) {
        Main.stage.close();

        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Main.stage.setScene(scene);
        Main.stage.show();
    }

    public void startClient() {
        try {
            if (serverIsAlive(Client.IP, Client.PORT)) {
                Main.client = new Client();
                new Thread(Main.client).start();
            }
        } catch (Exception e) {
            System.err.println("server is currently unavailable");
        }
    }

    /**
     *
     * @param color a string representing a color
     * @return color class
     */
    public Color StringToColor(String color) {

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

    public void friendRequestAccepted(String sender, String reciever) {
        Main.client.out.println("!accepted!!from!"+sender+" !to!"+reciever);
    }

    public void instantiateLocalData() {
        Main.localUser = new LocalUser();
    }
}
