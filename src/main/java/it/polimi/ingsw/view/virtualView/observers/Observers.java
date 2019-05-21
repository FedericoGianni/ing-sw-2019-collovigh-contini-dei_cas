package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.controller.Controller;

import java.util.HashMap;

public class Observers {

    private static HashMap<Integer, PlayerObserver> playerObservers = new HashMap<>();
    private static CurrentGameObserver gameObserver = new CurrentGameObserver();
    private static SpawnCellObserver spawnCellObserver = new SpawnCellObserver();
    private static AmmoCellObserver ammoCellObserver = new AmmoCellObserver();
    private final Controller controller;



    public Observers(Controller controller, int playerNum) {
        this.controller = controller;


        for (int i = 0; i < playerNum; i++) {

            playerObservers.put(i,new PlayerObserver(i,this));
        }

        gameObserver.setObservers(this);
        spawnCellObserver.setObservers(this);
        ammoCellObserver.setObservers(this);
    }

    public static StatsObserver getStatsObserver(int playerId){

        return playerObservers.get(playerId).getStatsObserver();
    }

    public static PowerUpBagObserver getPowerUpBagObserver(int playerId){

        return playerObservers.get(playerId).getPowerUpBagObserver();
    }

    public static AmmoBagObserver getAmmoBagObserver(int playerId){

        return playerObservers.get(playerId).getAmmoBagObserver();
    }

    public static CurrentGameObserver getGameObserver() {
        return gameObserver;
    }



    public Controller getController() {
        return controller;
    }

    public static Boolean isInitialized(){

        if (playerObservers.isEmpty()) return false;
        else return true;
    }

    public static SpawnCellObserver getSpawnCellObserver() {
        return spawnCellObserver;
    }

    public static AmmoCellObserver getAmmoCellObserver() {
        return ammoCellObserver;
    }
}
