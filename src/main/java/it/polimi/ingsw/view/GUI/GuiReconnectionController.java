package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.ProtocolType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import static it.polimi.ingsw.network.ProtocolType.RMI;
import static it.polimi.ingsw.network.ProtocolType.SOCKET;

public class GuiReconnectionController {



    private static Gui gui;

    private Stage myStage;

    private Scene mainScene;

    private ProtocolType protocolType = SOCKET;

    String playerName;

    PlayerColor playerColor;

    @FXML
    private ToggleGroup srmi;

    @FXML
    private TextField name;



    public static void setGui(Gui g) {
        gui = g;
    }

    public void setThirdScene(Scene scene){
        mainScene = scene;
    }

    public void setStageAndSetupListeners(Stage stage) {
        this.myStage = stage;
    }

    public void openThirdScene(ActionEvent actionEvent) {
        myStage.setScene(mainScene);
    }

    @FXML
    private void reconnect(ActionEvent actionEvent){

        // Connection choice

        int connectionchoice = 0;

        connectionchoice = srmi.getToggles().indexOf(srmi.getSelectedToggle());

        switch(connectionchoice) {

            case 0:
                protocolType = SOCKET;
                break;

            case 1:
                protocolType = RMI;
                break;
        }


        // Name Choice

        playerName = name.getText();

        gui.getView().createConnection(protocolType);
        gui.getView().reconnect(playerName);

    }

    @FXML
    private void chooseConnType(ActionEvent event){

        int choice;
        choice = srmi.getToggles().indexOf(srmi.getSelectedToggle());
        switch(choice) {

            case 0:
                protocolType = SOCKET;
                break;

            case 1:
                protocolType = RMI;
                break;
        }

    }
}
