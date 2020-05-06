package gui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Main;
import popup.CustomPasswordField;

public class SignUpWindow {

    Utils tools = new Utils();

    public String color;

    public Text usernameWarning;

    public SignUpWindow(Pane root) {

        TextField usernameTextfield = new TextField();
        usernameTextfield.setStyle("-fx-background-color: #CFD8DC; -fx-background-radius: 10;");
        tools.maxCharLength(usernameTextfield, 30);
        usernameTextfield.setLayoutX(45);
        usernameTextfield.setLayoutY(70);

        //warning text that appears when exceptions happen
        usernameWarning = new Text("");
        usernameWarning.setLayoutX(45);
        usernameWarning.setLayoutY(105);
        usernameWarning.setFill(Color.RED);
        usernameWarning.setFont(new Font("consolas", 13));

        correctTextfieldInput(usernameTextfield, usernameWarning);

        CustomPasswordField passwordTextfield = new CustomPasswordField();
        passwordTextfield.setStyle("-fx-background-color: #CFD8DC; -fx-background-radius: 10;");
        tools.maxCharLength(passwordTextfield, 30);
        passwordTextfield.setLayoutX(45);
        passwordTextfield.setLayoutY(130);

        //text appears when exceptions occur
        Text passwordWarning = new Text("");
        passwordWarning.setLayoutX(45);
        passwordWarning.setLayoutY(165);
        passwordWarning.setFill(Color.RED);
        passwordWarning.setFont(new Font("consolas", 13));

        correctTextfieldInput(passwordTextfield, passwordWarning);

        Text usernameText = new Text("username");
        usernameText.setFont(new Font("consolas", 15));
        usernameText.setFill(Color.web("#1e90ff"));
        usernameText.setLayoutX(45);
        usernameText.setLayoutY(60);

        Text passwordText = new Text("password");
        passwordText.setFont(new Font("consolas", 15));
        passwordText.setFill(Color.web("#1e90ff"));
        passwordText.setLayoutX(45);
        passwordText.setLayoutY(120);

        Text profileText = new Text("choose a new profile color");
        profileText.setFont(new Font("consolas", 15));
        profileText.setFill(Color.web("#1e90ff"));
        profileText.setLayoutX(45);
        profileText.setLayoutY(180);

        Text signupText = new Text("create an account");
        signupText.setLayoutX(15);
        signupText.setLayoutY(25);
        signupText.setFont(new Font("colsolas", 23));
        signupText.setFill(Color.web("#1e90ff"));

        HBox colorSelection = new HBox(10);
        colorSelection.setLayoutX(45);
        colorSelection.setLayoutY(190);

        Button blue = new Button();
        blue.setPrefWidth(35);
        setButtonColor(this, blue, "blue");
        blue.setStyle("-fx-background-color: blue; -fx-background-radius: 20;");

        Button red = new Button();
        red.setPrefWidth(35);
        setButtonColor(this, red, "red");
        red.setStyle("-fx-background-color: red; -fx-background-radius: 20;");

        Button green = new Button();
        green.setPrefWidth(35);
        setButtonColor(this, green, "green");
        green.setStyle("-fx-background-color: green; -fx-background-radius: 20;");

        Button orange = new Button();
        orange.setPrefWidth(35);
        setButtonColor(this, orange, "orange");
        orange.setStyle("-fx-background-color: orange; -fx-background-radius: 20;");

        Button black = new Button();
        black.setPrefWidth(35);
        setButtonColor(this, black, "black");
        black.setStyle("-fx-background-color: black; -fx-background-radius: 20;");

        colorSelection.getChildren().add(blue);
        colorSelection.getChildren().add(red);
        colorSelection.getChildren().add(green);
        colorSelection.getChildren().add(orange);
        colorSelection.getChildren().add(black);

        Button create = new Button("create");
        create.setPrefWidth(70);
        create.setLayoutX(45);
        create.setLayoutY(240);
        create.setStyle("-fx-background-color: #1E90FF;" +
                " -fx-background-radius: 10;" +
                " -fx-text-fill: whitesmoke;");

        /*
        creates a new user and adds it to the Server's database
         */
        create.setOnAction(new EventHandler<ActionEvent>() {

            //adds a user to the database
            @Override
            public void handle(ActionEvent event) {

                try {

                    if(tools.serverIsAlive(Client.IP, Client.PORT)) {

                        if (!usernameTextfield.getText().isEmpty() && !passwordTextfield.getText().isEmpty() && !color.isEmpty()) {
                            String name = usernameTextfield.getText();
                            String pass = passwordTextfield.getText();

                            tools.checkIfNameTaken(name);

                    /*
                        sends a request to the database to see if the username is taken
                     */

                            Thread.sleep(600);

                            if (Main.client.serverResponse.startsWith("!nametaken!false")) {

                                String r = Main.client.serverResponse;

                                //extract id from the server response
                                int id = Integer.parseInt(r.substring(21));

                                Main.localUser.setName(name);
                                Main.localUser.setPassword(pass);
                                Main.localUser.setColor(color);
                                Main.localUser.setId(id);

                                Main.app = new Gui(Main.root);

                                tools.createUser(name, pass, color);

                            } else if (Main.client.serverResponse.startsWith("!nametaken!true")) {
                                usernameWarning.setText("username is unavailable");
                            }
                        } else {
                            if (usernameTextfield.getText().isEmpty()) {
                                usernameWarning.setText("field not filled out");
                            }
                            if (passwordTextfield.getText().isEmpty()) {
                                passwordWarning.setText("field not filled out");
                            }
                        }
                    } else {
                        usernameWarning.setText("server is unavailable");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //changes the scene to login screen
        Button login = new Button("have an account?");
        login.setLayoutX(40);
        login.setLayoutY(270);
        login.setStyle("-fx-background-color: whitesmoke; -fx-underline: true;");
        login.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tools.changeScene(Main.login);
            }
        });

        root.getChildren().add(signupText);
        root.getChildren().add(usernameText);
        root.getChildren().add(passwordText);
        root.getChildren().add(profileText);
        root.getChildren().add(usernameTextfield);
        root.getChildren().add(usernameWarning);
        root.getChildren().add(passwordTextfield);
        root.getChildren().add(passwordWarning);
        root.getChildren().add(colorSelection);
        root.getChildren().add(create);
        root.getChildren().add(login);
    }

    /**
     * adds a listener to the button
     *
     * @param window
     * @param button
     * @param color
     */
    public void setButtonColor(SignUpWindow window, Button button, String color) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.color = color;
            }
        });
    }

    /**
     * adds a listener to the given textfield that removes
     * exclaimation marks and spaces in the text
     *
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

        textfield.addEventFilter(KeyEvent.KEY_RELEASED, passwordEventFilter);
    }
}
