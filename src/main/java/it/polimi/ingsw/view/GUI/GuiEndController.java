package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.cachemodel.Player;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
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
        List<Player> winners = new ArrayList<>();
        int maxScore = 0;

        for(Player p : players){
            String s = new String();
            s = s.concat(" ID: " + p.getPlayerId());
            s = s.concat(" Nome: " + p.getName());
            s = s.concat(" Punti: " + p.getStats().getScore());
            s = s.concat(" Punti KillShotTrack: " + calcKillShotTrackPoints(gui.getView().getPlayerId()));

            pointPlayers.add(s);

            if(p.getStats().getScore() >= maxScore){
                maxScore = p.getStats().getScore();
            }
        }
    }

    /**
     *
     * @param playerId id of the player to calc points
     * @return the number of points done inside killShotTrack durig the game
     */
    private int calcKillShotTrackPoints(int playerId){

        int points = 0;

        if(gui.getView().getCacheModel().getGame() != null){
            for (int i = 0; i < gui.getView().getCacheModel().getGame().getKillShotTrack().size(); i++) {
                if(gui.getView().getCacheModel().getGame().getKillShotTrack().get(i).x == playerId){
                    points += gui.getView().getCacheModel().getGame().getKillShotTrack().get(i).y;
                }
            }
        }

        return points;

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
