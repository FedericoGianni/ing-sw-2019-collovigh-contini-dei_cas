package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.RunClient;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

public class GuiMessages implements Runnable {

    String infoMessage;
    String titleBar;

    public GuiMessages(String infoMessage, String titleBar) {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        this.infoMessage = infoMessage;
        this.titleBar = titleBar;
    }

     @Override
     public void run(){
        infoBox();
     }

    public void infoBox() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(this.titleBar);
        //alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}