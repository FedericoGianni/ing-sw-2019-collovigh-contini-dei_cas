package it.polimi.ingsw.view.virtualView;

import java.util.ArrayList;
import java.util.HashMap;

public class Observers {

    private static HashMap<Integer,PlayerObserver> playerObservers = new HashMap<>();


    public Observers() {
    }

    public static StatsObserver getStatsObserver(int playerId){


        if (playerObservers.containsKey(playerId)){

            if (playerObservers.get(playerId).getStatsObserver() != null){

                return playerObservers.get(playerId).getStatsObserver();

            }else {

                playerObservers.get(playerId).setStatsObserver(new StatsObserver(playerId));

                return playerObservers.get(playerId).getStatsObserver();
            }

        }else{

            playerObservers.put(playerId,new PlayerObserver());

            playerObservers.get(playerId).setStatsObserver(new StatsObserver(playerId));

            return playerObservers.get(playerId).getStatsObserver();

        }
    }


    
}
