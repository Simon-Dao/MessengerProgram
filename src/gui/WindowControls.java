package gui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.Main;

public class WindowControls {

    Stage stage;
    Scene scene;
    Utils tools = new Utils();

    /*
       closes the window when esc is pressed
     */

    public WindowControls(Stage stage, Scene scene) {
        this.scene = scene;
        this.stage = stage;

        stage.setOnCloseRequest(event -> {
            if(stage.getScene().equals(Main.messengerWindow)) {
                tools.sendMessage("!offline!"+Main.localUser.getName());
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keys -> {
            if(keys.getCode() == KeyCode.ESCAPE) {

                if(stage.getScene().equals(Main.messengerWindow)) {
                    tools.sendMessage("!offline!"+Main.localUser.getName());
                }

                stage.close();
                System.exit(0);
            }
        });
    }
}
