package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;


public class AddFriendWindow {

    Main main;
    Utils tools = new Utils();

    public AddFriendWindow(String name) {

        Stage window = new Stage();
        window.setResizable(false);
        window.initStyle(StageStyle.UTILITY);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMaxWidth(200);
        window.setMaxHeight(100 );

        Pane p = new Pane();

        Text sendMessageText = new Text();

        Text addText = new Text(name);
        addText.setLayoutX(20);
        addText.setLayoutY(20);
        addText.setFont(new Font("consolas",20));
        p.getChildren().add(addText);

        Button sendButton = new Button("message");
        sendButton.setLayoutX(20);
        sendButton.setLayoutY(30);
        sendButton.setMaxWidth(75);
        sendButton.setStyle("-fx-background-color: #19b5fe;" +
                "    -fx-background-radius: 5;" +
                "    -fx-text-fill: white;" +
                "    -fx-font-family: Open Sans;");
        p.getChildren().add(sendButton);

        Text nameText = new Text();

        Button addButton = new Button("+");
        addButton.setLayoutX(90);
        addButton.setLayoutY(30);
        addButton.setMaxWidth(30);
        addButton.setStyle("-fx-background-color: #19b5fe;" +
                "    -fx-background-radius: 5;" +
                "    -fx-text-fill: white;" +
                "    -fx-font-family: Open Sans;");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tools.sendMessage("!friendrequest!!sender!" + Main.localUser.getName()
                        + " !reciever!" + name);
            }
        });

        p.getChildren().add(addButton);

        Button blockButton = new Button("block");
        blockButton.setLayoutX(120);
        blockButton.setLayoutY(30);
        blockButton.setMaxWidth(60);
        blockButton.setStyle("-fx-background-color: #19b5fe;" +
                "    -fx-background-radius: 5;" +
                "    -fx-text-fill: white;" +
                "    -fx-font-family: Open Sans;");
        p.getChildren().add(blockButton);

        Scene scene = new Scene(p,200,100);

        window.setScene(scene);
        window.show();
    }
}
