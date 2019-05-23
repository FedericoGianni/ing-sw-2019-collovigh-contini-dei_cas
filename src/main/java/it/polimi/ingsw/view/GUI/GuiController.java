package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.ProtocolType;
import it.polimi.ingsw.network.RunClient;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import javafx.event.ActionEvent;


public class GuiController {

    private View view;

    private Gui gui;

    @FXML
    private void login(ActionEvent event){
        System.out.println("[DEBUG] LOGIN CLICCATO!");
        gui.getView().joinGame("test", PlayerColor.BLUE);
    }

    @FXML
    private void chooseSocket(ActionEvent event){
        System.out.println("[DEBUG] SOCKET CLICCATO!");
        //gui.getView().createConnection(ProtocolType.SOCKET);
        view.createConnection(ProtocolType.SOCKET);
    }

    @FXML
    private void chooseRMI(ActionEvent event){
         System.out.println("[DEBUG] RMI CLICCATO!");
         gui.getView().createConnection(ProtocolType.RMI);
    }

}
