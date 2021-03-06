package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import database.Friend;
import main.Main;

import java.util.ArrayList;

public class Contacts {

    private VBox layout;
    private VBox contact;
    private ScrollPane friends;
    private Utils tools = new Utils();

    public Contacts(VBox contacts) {
        this.layout = contacts;
        layout.setId("ContactsPane");

        friends = createTopSection();
        Pane userInfo = createBottomSection();

        layout.getChildren().add(friends);
        layout.getChildren().add(userInfo);
    }

    private ScrollPane createTopSection() {
        ScrollPane root = new ScrollPane();

        VBox top = new VBox();
        top.setId("FriendScrollPane");
        top.setPrefHeight(420);
        contact = new VBox();

        top.getChildren().add(contact);

        root.setContent(top);

        return root;
    }

    private Pane createBottomSection() {
        Pane bottom = new Pane();
        bottom.setId("bottomPane");
        bottom.setPrefHeight(80);

        Circle profileColor = new Circle(20,20,15, Main.localUser.getColor());

        //name color settings
        Text username = new Text(Main.localUser.getName());
        username.setFont(new Font("consolas", 15));
        username.setFill(Color.WHITESMOKE);
        username.setLayoutX(40);
        username.setLayoutY(23);

        bottom.getChildren().add(profileColor);
        bottom.getChildren().add(username);

        return bottom;
    }

    public void addContact(String username, String color) {

        if(friendExists(username)) {

            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: #DCEAEA ;");
            pane.setPrefWidth(226);
            pane.setMinHeight(50);
            pane.setPrefHeight(50);
            pane.setMaxHeight(50);

            Circle profile = new Circle(15);
            profile.setLayoutX(20);
            profile.setLayoutY(22);
            profile.setFill(tools.StringToColor(color));

            Text name = new Text(username);
            name.setFont(new Font("consolas", 17));
            name.setLayoutX(40);
            name.setLayoutY(18);
            name.setFill(Color.BLACK);

            Text message = new Text("message");
            message.setFont(new Font("consolas", 12));
            message.setLayoutX(40);
            message.setLayoutY(35);
            message.setFill(Color.BLUE);

            pane.getChildren().add(profile);
            pane.getChildren().add(name);
            pane.getChildren().add(message);
            pane.addEventFilter(MouseEvent.MOUSE_CLICKED, mouse -> {
                //TODO make it turn gray when selected
                System.out.println("showing messages");
            });

            contact.getChildren().add(pane);
        }
    }

    private boolean friendExists(String name) {

        ArrayList<Friend> friends = Main.localUser.friends;
        boolean available = true;

        for(int i=0; i<friends.size();i++) {
            if(friends.get(i).username.equals(name)) {
                available = false;
            }
        }

        return available;
    }
}
