package it.polimi.ingsw.view.cacheModel;

import java.util.ArrayList;
import java.util.List;

public class CacheModel {

    static private List<Player> players;
    private final int mapType;


    public CacheModel(InitialUpdate update) {

        players = new ArrayList<>();

        for (int i = 0; i < update.getNames().size() ; i++) {

            players.add(new Player(i,update.getNames().get(i),update.getColors().get(i)));

        }

        this.mapType = update.getMapType();
    }

    public static List<Player> getCachedPlayers(){

        return players;
    }

    //public static void update()
}
