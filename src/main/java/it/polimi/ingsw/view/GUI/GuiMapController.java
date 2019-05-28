package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.player.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;




public class GuiMapController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }

    @FXML
    GridPane mappozza;
    @FXML
    BorderPane pane;
    @FXML
    TextArea log;

    @FXML
    public void initialize() {

    }

    public void mapCreator()
    {

        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {
                mappozza.setStyle("-fx-background-image: url('/images/Map1.png')");
                break;
            }
            case 2: {
                mappozza.setStyle("-fx-background-image: url('/images/Map2.png')");
                break;
            }
            case 3: {
                mappozza.setStyle("-fx-background-image: url('/images/Map3.png')");
                break;
            }
        }
    }

    public void loginUpdater(String name, int id, PlayerColor color)
    {
        log.appendText("\nSi è collegato: "+name+" con l'id: "+id+" ed il colore: "+color);
    }



}
