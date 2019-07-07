package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.utils.PlayerColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import static it.polimi.ingsw.network.ProtocolType.RMI;
import static it.polimi.ingsw.network.ProtocolType.SOCKET;

public class GuiReconnectionController {

    /**
     * Reference to the gui linked to this controller
     */
    private static Gui gui;

    /**
     * Reference to the stage needed to switch scenes
     */
    private Stage myStage;

    /**
     * Reference to the main game scene
     */
    private Scene mainScene;

    /**
     * Protocol type chosen by the user to connect (default is set to SOCKET)
     */
    private ProtocolType protocolType = SOCKET;

    /**
     * Name chosen by the user
     */
    String playerName;

    /**
     * Color chosen by the user
     */
    PlayerColor playerColor;

    /**
     * Toggle Group to choose the Connection type (socket/rmi)
     */
    @FXML
    private ToggleGroup srmi;

    /**
     * Textflied in which user will input its name
     */
    @FXML
    private TextField name;

    public String getPlayerName() {
        return playerName;
    }

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

    /**
     * Reconnect the player using playerName (his old name before disconnecting)
     * @param actionEvent
     */
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
        new Thread(() -> gui.getView().reconnect(playerName)).start();

    }

    /**
     * Choose the connection type from the Toggle Group srmi
     * @param event
     */
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
