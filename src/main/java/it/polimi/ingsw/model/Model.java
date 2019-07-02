package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Model {

    private static CurrentGame game;

    /**
     * Default constructor
     */
    public Model(List<String> playerNames, List<PlayerColor> playerColors, int mapType, int skulls) {

        Map map = new Map(mapType);

        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            playerList.add(new Player(playerNames.get(i), i, playerColors.get(i)));
        }

        game = new CurrentGame(playerList, map, skulls);

        if (Observers.isInitialized()) game.addObserver(Observers.getGameObserver());

        if (Observers.isInitialized()) map.updateObserver();

        if (Observers.isInitialized()) map.initializeSpawnCell();


        //TODO remove this, just to kill faster

        for(Player p : playerList){
            p.addDmg(0, 10);
        }
    }

    public static CurrentGame getGame() {
        return game;
    }

    public static void setGame(CurrentGame game) { Model.game = game;}

    public static Map getMap() {
        return getGame().getMap();
    }

    public static Player getPlayer(int playerId){ return getGame().getPlayers().get(playerId);}
}