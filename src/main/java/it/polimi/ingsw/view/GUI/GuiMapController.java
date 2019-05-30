package it.polimi.ingsw.view.GUI;


import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;


public class GuiMapController {

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }


    @FXML
    BorderPane pane;
    @FXML
    TextArea log;
    @FXML
    GridPane innerMap;
    @FXML
    BorderPane mappozzaExt;
    @FXML
    public void initialize() {

    }

    public void mapCreator()
    {


        switch(gui.getView().getCacheModel().getMapType()) {
            case 1: {

                break;
            }
            case 2: {
                mappozzaExt.setStyle("-fx-background-image: url('/images/Map2ext2.png')");
                innerMap.setStyle("-fx-background-image: url('/images/Map2Inside.png')");
                break;
            }
            case 3: {
                mappozzaExt.setStyle("-fx-background-image: url('/images/Map3.png')");
                break;
            }
        }
    }

    public void loginUpdater(String name, int id, PlayerColor color)
    {
        log.appendText("\nSi è collegato: "+name+" con l'id: "+id+" ed il colore: "+color);
    }

    public void statsUpdater(Player player)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setContentText("Updated player stats");

        alert.showAndWait();

    }

    @FXML
    public void click1()
    {
        log.appendText("cliccato 1");
    }


}
