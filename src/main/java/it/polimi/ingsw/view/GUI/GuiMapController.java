package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;


public class GuiMapController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }

    @FXML
    GridPane mappozza;

    @FXML
    public void initialize() {

        switch(gui.getView().getCacheModel().getMapType())
        {
            case 1:
            {mappozza.setStyle("-fx-background-image: url('/images/Map1.png')"); break;}
            case 2:
            {mappozza.setStyle("-fx-background-image: url('/images/Map2.png')"); break;}
            case 3:
            {mappozza.setStyle("-fx-background-image: url('/images/Map3.png')"); break;}
        }


    }




}
