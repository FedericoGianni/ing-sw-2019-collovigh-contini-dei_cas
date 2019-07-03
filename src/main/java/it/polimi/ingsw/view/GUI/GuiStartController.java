package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiStartController {

    /**
     * Reference to the gui linked to this controller
     */
    private static Gui gui;

    /**
     * Reference to the login scene
     */
    private Scene loginScene;

    /**
     * Reference to the reconnection scene
     */
    private Scene reconnectScene;

    /**
     * Reference to the stage needed to switch scenes
     */
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

    /**
     * Open Login scene after the user has chosen to Login in a new game
     * @param actionEvent
     */
    public void openLoginScene(ActionEvent actionEvent) {
        myStage.setScene(loginScene);
    }

    /**
     * Open Reconnection scene if the user wants to reconnect to a current game
     * @param actionEvent
     */
    public void openReconnectScene(ActionEvent actionEvent) {
        myStage.setScene(reconnectScene);
    }

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
    }
}
