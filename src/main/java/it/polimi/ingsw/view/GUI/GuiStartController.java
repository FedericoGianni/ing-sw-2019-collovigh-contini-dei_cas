package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiStartController {

    private static Gui gui;

    private Scene loginScene;

    private Scene reconnectScene;

    private Stage myStage;


    public static void setGui(Gui g) {
        gui = g;
    }

    public void setLoginScene(Scene scene){
        this.loginScene = scene;
    }

    public void setReconnectScene(Scene scene){
        this.reconnectScene = scene;
    }

    public void openLoginScene(ActionEvent actionEvent) {
        myStage.setScene(loginScene);
    }

    public void openReconnectScene(ActionEvent actionEvent) {
        myStage.setScene(reconnectScene);
    }

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
    }
}
