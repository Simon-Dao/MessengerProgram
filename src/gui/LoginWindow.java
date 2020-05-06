package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;
import client.Client;
import popup.CustomPasswordField;

public class LoginWindow {

    Utils tools = new Utils();

    /**
     * creates a login window gui
     *
     * @param root
     */
    public LoginWindow(Pane root) {

        Text loginText = new Text("Login");
        loginText.setFont(new Font("consolas", 20));
        loginText.setFill(Color.web("#1e90ff"));
        loginText.setLayoutX(80);
        loginText.setLayoutY(60);

        TextField usernameTextfield = new TextField();
        usernameTextfield.setStyle("-fx-background-color: #CFD8DC; -fx-background-radius: 10;");
        usernameTextfield.setPromptText("username");
        usernameTextfield.setLayoutX(80);
        usernameTextfield.setLayoutY(80);
        tools.maxCharLength(usernameTextfield, 30);

        Text usernameWarning = new Text("");
        usernameWarning.setLayoutX(80);
        usernameWarning.setLayoutY(117);
        usernameWarning.setFill(Color.RED);
        usernameWarning.setFont(new Font("consolas", 13));

        //prevents username textfield from taking in ! and " " for security
        correctTextfieldInput(usernameTextfield, usernameWarning);

        CustomPasswordField passwordTextfield = new CustomPasswordField();
        passwordTextfield.setStyle("-fx-background-color: #CFD8DC; -fx-background-radius: 10;");
        passwordTextfield.setPromptText("password");
        passwordTextfield.setLayoutX(80);
        passwordTextfield.setLayoutY(125);

        Text passwordWarning = new Text("");
        passwordWarning.setLayoutX(80);
        passwordWarning.setLayoutY(162);
        passwordWarning.setFill(Color.RED);
        passwordWarning.setFont(new Font("consolas", 13));

        correctTextfieldInput(passwordTextfield, passwordWarning);

        Button apply = new Button("apply");
        apply.setStyle("-fx-background-color: #1E90FF; -fx-background-radius: 5; -fx-text-fill: whitesmoke;");
        apply.setLayoutX(80);
        apply.setLayoutY(180);
        apply.setPrefWidth(150);
        apply.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (tools.serverIsAlive(Client.IP, Client.PORT)) {

                    if (usernameTextfield.getText().length() != 0 && passwordTextfield.getText().length() != 0) {

                        tools.sendUserData(usernameTextfield.getText(), passwordTextfield.getText());

                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //System.out.println("[LOGINWINDOW] id:"+Main.client.serverResponse.substring(42));
                        if (Main.client.serverResponse.startsWith("!userIsVerified!true")) {

                            String r = Main.client.serverResponse;
                            //parse the id from the server response
                            int id = Integer.parseInt(r.substring(r.substring(32).indexOf(" ")+37));
                            String color = r.substring(32,r.substring(32).indexOf(" ")+32);

                            Main.localUser.setName(usernameTextfield.getText());
                            Main.localUser.setPassword(passwordTextfield.getText());
                            Main.localUser.setColor(color);
                            Main.localUser.setId(id);

                            Main.app = new Gui(Main.root);

                            tools.changeScene(Main.messengerWindow);

                        } else if (Main.client.serverResponse.startsWith("!userIsVerified!false")) {
                            usernameWarning.setText("user could not be verified");
                        }
                    } else {
                        if (passwordTextfield.getText().isEmpty()) {
                            passwordWarning.setText("field is empty");
                        }
                        if (usernameTextfield.getText().isEmpty()) {
                            usernameWarning.setText("field is empty");
                        }
                    }
                } else {
                    System.err.println("server is currently unavailable");
                    usernameWarning.setText("server is unavailable");
                }
            }
        });

        Button signup = new Button("sign up!");
        signup.setStyle("-fx-background-color: whitesmoke; -fx-underline: true");
        signup.setLayoutX(127);
        signup.setLayoutY(210);
        signup.setOnAction(new EventHandler<ActionEvent>() {

            //changes scene to signup window
            @Override
            public void handle(ActionEvent event) { tools.changeScene(Main.signup); }});

        root.getChildren().add(loginText);
        root.getChildren().add(usernameTextfield);
        root.getChildren().add(usernameWarning);
        root.getChildren().add(passwordTextfield);
        root.getChildren().add(passwordWarning);
        root.getChildren().add(apply);
        root.getChildren().add(signup);
    }

    /**
     * adds a listener to the textfield that detects and removes
     * exclaimation points and spaces in the text
     * @param textfield
     * @param warningText
     */
    public void correctTextfieldInput(TextField textfield, Text warningText) {

        EventHandler<KeyEvent> passwordEventFilter = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                //if you type a space or a exclamation point then put up an error message
                try {
                    String text = textfield.getText();

                    if (text.contains(" ")) {
                        warningText.setText("cannot use space");

                        textfield.setText(text.replace(" ", ""));

                    } else if (text.contains("!")) {
                        warningText.setText("cannot use ! symbol");
                        textfield.setText(text.replace("!", ""));
                    } else {
                        warningText.setText("");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                }
            }
        };
    }
}
