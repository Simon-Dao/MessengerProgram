package gui;

import client.Client;
import client.LocalUser;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import main.Main;
import popup.TextFields;

import java.util.ArrayList;

//TODO make the user send data to the friend

public class SearchBarSection {

    public TextField searchTextfield;
    private String[] suggestions = null;
    private Utils tools = new Utils();

    private MenuButton notificationsMenu;

    /**
     * takes a pane and adds nodes to it
     *
     * @param searchBarLayout
     */
    public SearchBarSection(Pane searchBarLayout) {
        //textfield that searches for other users
        searchTextfield = new TextField();
        searchTextfield.setPromptText("search for people!");
        searchTextfield.setLayoutX(10);
        searchTextfield.setLayoutY(26);
        addSuggestionsDropDown(searchTextfield);

        //button that sends a friend request to friends
        Button searchButton = new Button("search");
        searchButton.setId("searchButton");
        searchButton.setLayoutX(160);
        searchButton.setLayoutY(27);
        searchButtonEvent(searchButton);
        searchButton.setFont(new Font("consolas", 10));

        //button that shows notifications
        notificationsMenu = new MenuButton("notifications");
        notificationsMenu.setId("notificationMenu");
        notificationsMenu.setLayoutX(380);
        notificationsMenu.setLayoutY(26);

        /*
        created a dropdown showing results
        based on the user input of the textfield
         */
        searchBarLayout.getChildren().add(searchButton);
        searchBarLayout.getChildren().add(searchTextfield);
        searchBarLayout.getChildren().add(notificationsMenu);
    }

    /**
     * adds a dropdown menu displaying the
     * suggested names the user is searching for
     *
     * @param field is the textfield provided from the constructor
     */
    private void addSuggestionsDropDown(TextField field) {

        /*
        event handler listens for key input and creates a list
        of suggestions the user might be looking for
         */
        EventHandler searchBarEvent = new EventHandler() {
            @Override
            public void handle(Event event) {

                suggestions = getUserKeys();

                TextFields.bindAutoCompletion(field, suggestions);
            }
        };

        field.addEventFilter(KeyEvent.KEY_RELEASED, searchBarEvent);
    }

    public String[] getUserKeys() {

        tools.sendMessage("!keyrequest!");

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //receives a list of
        if (Client.serverResponse.startsWith("!length!")) {

            String x = Client.serverResponse;

            int length = Integer.parseInt(x.substring(8, 9));

            suggestions = new String[length];

            int divider = 19;

            for (int i = 0; i < length; i++) {
                String y = x.substring(divider);
                suggestions[i] = x.substring(divider, y.indexOf(".") + divider);
                divider += y.indexOf(".") + 1;
            }
        }
        return suggestions;
    }

    private void searchButtonEvent(Button button) {

        EventHandler event = new EventHandler() {
            @Override
            public void handle(Event event) {

                tools.sendMessage("!friendrequest!!sender!" + Main.localUser.getName()
                        + " !reciever!" + searchTextfield.getText());
            }
        };
        button.setOnAction(event);
    }

    public void addNotification(String name, String color) {
        MenuItem notification = new MenuItem(name + " add+");

        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                notificationsMenu.getItems().remove(notification);
                Main.localUser.addFriend(name, color);
            }
        };
        notification.setOnAction(event1);

        notificationsMenu.getItems().add(notification);
    }
}
