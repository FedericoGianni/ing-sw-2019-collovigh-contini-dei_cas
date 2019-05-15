package it.polimi.ingsw.view.virtualView;

import java.util.HashMap;

public class Observers {

    private static HashMap<Integer,PlayerObserver> playerObservers = new HashMap<>();
    private static CurrentGameObserver gameObserver = new CurrentGameObserver();


    public Observers(int playerNum) {

        for (int i = 0; i < playerNum; i++) {

            playerObservers.put(i,new PlayerObserver());
        }
    }

    public static StatsObserver getStatsObserver(int playerId){

        return createIfNotPresent(playerId).getStatsObserver();
    }

    public static PowerUpBagObserver getPowerUpBagObserver(int playerId){

        return createIfNotPresent(playerId).getPowerUpBagObserver();
    }

    public static AmmoBagObserver getAmmoBagObserver(int playerId){

        return createIfNotPresent(playerId).getAmmoBagObserver();
    }

    public static CurrentGameObserver getGameObserver() {
        return gameObserver;
    }

    private static PlayerObserver createIfNotPresent(int playerId){

        if (playerObservers.containsKey(playerId)){

            return playerObservers.get(playerId);
            
        }else {

            playerObservers.put(playerId, new PlayerObserver());

            return playerObservers.get(playerId);
        }


    }
}
