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


public class GuiController {

    /**
     * Reference to the gui linked to this controller
     */
    private static Gui gui;

    /**
     * set the gui parameter
     * @param g gui to set as reference
     */
    public static void setGui(Gui g) {
        gui = g;
    }

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
     * Refeerence to the stage
     */
    private Stage myStage;

    /**
     * Reference to the Lobby scene to switch windows
     */
    private Scene secondScene;

    private Scene reconnectScene;

    /**
     * Set the lobby scene
     * @param scene scene to be set
     */
    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }

    public void setReconnectScene(Scene reconnectScene) {
        this.reconnectScene = reconnectScene;
    }

    @FXML
    private ToggleGroup color;

    @FXML
    private ToggleGroup srmi;

    @FXML
    private TextField name;

    @FXML
    private void login(ActionEvent event){

        // Connection choice
        playerName = name.getText();
        if(playerName.equals(""))
        {
            gui.retryLogin("Nome vuoto!");
            return;
        }
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



        // Color Choice

        int colorChoice;

        System.out.println("colore scelto : " + color.getToggles().indexOf(color.getSelectedToggle()));

        colorChoice = color.getToggles().indexOf(color.getSelectedToggle());

        switch(colorChoice) {
            case 0:
                playerColor = PlayerColor.BLUE;
                break;
            case 1:
                playerColor = PlayerColor.GREEN;
                break;
            case 2:
                playerColor = PlayerColor.PURPLE;
                break;
            case 3:
                playerColor = PlayerColor.YELLOW;
                break;
            case 4:
                playerColor = PlayerColor.GREY;
                break;
        }

        // Name Choice



        gui.getView().createConnection(protocolType);
        gui.getView().joinGame(playerName, playerColor);

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

    /**
     * Open Lobby scene
     * @param actionEvent any event to trigger this method
     */
    public void openSecondScene(ActionEvent actionEvent) {
        myStage.setScene(secondScene);}

    public void openReconnectScene(ActionEvent actionEvent) {
        myStage.setScene(reconnectScene);}

    /**
     * Controller initializer
     * @param stage stage to be set
     */
    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
    }
}


