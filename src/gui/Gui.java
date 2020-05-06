package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;

public class Gui {

    public Utils tools = new Utils();
    private static TextField textField;
    public Contacts contactsArea;
    public MessageArea messageArea;
    public SearchBarSection searchBarLayout;

    /**
     * creates a gui
     * @param root is the node that all the other panes build off of
     */
    public Gui(VBox root) {

        Pane searchBar = createTopHalf();
        searchBar.setId("me");

        SplitPane messageAndContacts = createBottomHalf();

        root.getChildren().add(searchBar);
        root.getChildren().add(messageAndContacts);
    }

    /**
     * creates the UI for the searchbar section
     * @return pane
     */
    public Pane createTopHalf() {
        Pane pane = new Pane();
        pane.setPrefHeight(60);

        Text searchText = new Text("");
        searchText.setLayoutX(10);
        searchText.setLayoutY(20);
        searchText.setFill(Color.WHITESMOKE);
        searchText.setFont(new Font("consolas",18));
        pane.getChildren().add(searchText);

        searchBarLayout = new SearchBarSection(pane);

        return pane;
    }

    /**
     * creates the message and contacts areas
     * @return
     */
    public SplitPane createBottomHalf() {
        SplitPane messageAndContacts = new SplitPane();
        messageAndContacts.setPrefHeight(440);
        //left side
        VBox contacts = new VBox();
        //contacts.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        contactsArea = new Contacts(contacts);

        //right side
        VBox rightSide = new VBox();

        //chat area
        ScrollPane messages = new ScrollPane();
        messageArea = new MessageArea(messages);

        //bottom text field
        Pane bottomMessageArea = new Pane();

        textField = new TextField();
        tools.maxCharLength(textField, 30);
        textField.setPromptText("send a message!");

        Button sendButton = new Button("send");
        sendButton.setLayoutX(160);
        sendButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //only send a message if the textfield isn't empty
                if (textField.getText().length() != 0) {
                    System.out.println(Main.localUser.getColor() == null);
                    messageArea.addMessage(Main.localUser.getName(), textField.getText(), Main.localUser.getColor());
                    tools.sendMessage(Main.localUser.getName(), Main.localUser.getColorInString(), textField.getText());
                    textField.clear();
                }
            }
        });

        bottomMessageArea.getChildren().add(textField);
        textField.getParent().requestFocus();
        bottomMessageArea.getChildren().add(sendButton);

        AnchorPane messageBox = new AnchorPane();
        messageBox.setBottomAnchor(bottomMessageArea, (double) 35);
        messageBox.setTopAnchor(bottomMessageArea, (double) 10);
        messageBox.setLeftAnchor(bottomMessageArea, (double) 10);
        messageBox.setRightAnchor(bottomMessageArea, (double) 30);

        messageBox.getChildren().add(bottomMessageArea);

        messageAndContacts.getItems().add(contacts);
        messageAndContacts.getItems().add(rightSide);

        rightSide.getChildren().add(messages);
        rightSide.getChildren().add(messageBox);

        return messageAndContacts;
    }
}
