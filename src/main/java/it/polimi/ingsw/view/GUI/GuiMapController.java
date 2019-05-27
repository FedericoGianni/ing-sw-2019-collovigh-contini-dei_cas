package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class GuiMapController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }


    @FXML
    private void handleButtonAction(ActionEvent action)
    {
        System.out.println("A breve ;)");
    }




}
