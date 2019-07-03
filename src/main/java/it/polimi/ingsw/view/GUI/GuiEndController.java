package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.cachemodel.Player;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;


public class GuiEndController {

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
     * ListView to display the observable list in the graphical user interface
     */
    @FXML
    private ListView pointPlayersListView;

    /**
     * List of Players with their points
     */
    private static ObservableList<String> pointPlayers = FXCollections.observableArrayList();

    /**
     * Add Player ID, Name and Total score to the observable list, then it will be automatically updated by the ListView
     * @param event
     */
    @FXML
    public void showPoints(ActionEvent event){

        List<Player> players = gui.getView().getCacheModel().getCachedPlayers();

        for(Player p : players){
            String s = new String();

            s = s.concat(" ID: " + p.getPlayerId());
            s = s.concat("\tNome: " + p.getName());
            s = s.concat("\tPunti: " + p.getStats().getScore());

            pointPlayers.add(s);
        }
    }

    /**
     * GuiEndController initializer
     */
    public void setUp(){
        pointPlayersListView.setItems(pointPlayers);
        pointPlayers.addListener((ListChangeListener<String>) c -> {pointPlayersListView.refresh();});
    }

    /**
     * Close the windows and exit from GUI
     * @param event any event (in this case button pressed)
     */
    @FXML
    public void close(ActionEvent event){
        System.exit(0);
    }


}
