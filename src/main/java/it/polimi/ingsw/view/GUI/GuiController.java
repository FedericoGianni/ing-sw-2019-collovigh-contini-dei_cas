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


public class GuiController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }

    private ProtocolType protocolType = SOCKET;
    String playerName;
    PlayerColor playerColor;

    private Stage myStage;

    private Scene secondScene;

    public void setSecondScene(Scene scene) {
        secondScene = scene;
    }


    @FXML
    private ToggleGroup color;

    @FXML
    private ToggleGroup srmi;

    @FXML
    private TextField name;

    @FXML
    private void login(ActionEvent event){

        int colorChoice;
        //System.out.println("[DEBUG] LOGIN CLICCATO!");
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

        playerName = name.getText();

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

    public void openSecondScene(ActionEvent actionEvent) {
        myStage.setScene(secondScene);
    }

    public void setStageAndSetupListeners(Stage stage){
        this.myStage = stage;
    }
}


