package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiLobbyController {

    private static Gui gui;
    public static void setGui(Gui g) {
        gui = g;
    }

    private Stage myStage;

    private Scene loginScene;
    private Scene mainScene;

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
    }

    public void setFirstScene(Scene scene) {
        loginScene = scene;
    }

    public void openFirstScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);
    }

    public void setThirdScene(Scene scene){
        mainScene = scene;
    }

    public void openThirdScene(ActionEvent actionEvent) {
        myStage.setScene(mainScene);
    }
}
