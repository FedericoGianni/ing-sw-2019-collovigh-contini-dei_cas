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

    private static Gui gui;

    public static void setGui(Gui g) {
        gui = g;
    }

    @FXML
    private ListView pointPlayersListView;

    private static ObservableList<String> pointPlayers = FXCollections.observableArrayList();

    @FXML
    public void showPoints(ActionEvent event){
        //TODO aggiungere stringa con nomi e punti dei giocatori -> se si aggiorna la lista pointPlayers in automatico
        //dovrebbe anche aggiornare la visualizzazione dei nomi sulla gui

        List<Player> players = gui.getView().getCacheModel().getCachedPlayers();

        for(Player p : players){
            String s = new String();

            s = s.concat(" ID: " + p.getPlayerId());
            s = s.concat("\tNome: " + p.getName());
            s = s.concat("\tPunti: " + p.getStats().getScore());

            pointPlayers.add(s);
        }
    }

    public void setUp(){
        pointPlayersListView.setItems(pointPlayers);
        pointPlayers.addListener((ListChangeListener<String>) c -> {pointPlayersListView.refresh();});
    }

    @FXML
    public void close(ActionEvent event){
        System.exit(0);
    }


}
