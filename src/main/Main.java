package main;

//import gui.*;
import client.Client;
import client.LocalUser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import gui.SignUpWindow;
import gui.Utils;
import gui.WindowControls;
import gui.Gui;
import gui.LoginWindow;

public class Main extends Application {

    public static Client client;
    public static Stage stage;
    public static Scene messengerWindow;
    public static Scene login;
    public static Scene signup;

    private Utils utils = new Utils();

    public static Pane signupPane;
    public static SignUpWindow signupWindow;
    public static Gui app;
    public static LocalUser localUser;
    public static VBox root;

    /*
        TODO add a friend system
     */

    /**
     * starts up the stage and loads the scenes.
     * also created a connection to the server
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        //starts the client and stores userdata locally
        utils.startClient();
        utils.instantiateLocalData();

        //instantiates the signup window
        signupPane = new Pane();
        signupWindow = new SignUpWindow(signupPane);
        signup = new Scene(signupPane, 300, 300);
        new WindowControls(stage, signup);

        //instantiates the login window
        Pane loginLayout = new Pane();
        new LoginWindow(loginLayout);
        login = new Scene(loginLayout, 300, 250);
        new WindowControls(stage, login);

        //instantiates the messenger window
        root = new VBox();
        messengerWindow = new Scene(root, 500, 500);
        messengerWindow.getStylesheets().add(this.getClass()
                .getResource("Style.css").toExternalForm());
        new WindowControls(stage, messengerWindow);

        //sets the scene
        stage.setTitle("Messenger");
        stage.setScene(login);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}