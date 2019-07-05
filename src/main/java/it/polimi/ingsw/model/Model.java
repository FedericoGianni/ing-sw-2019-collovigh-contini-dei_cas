package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used across the model to access all the part of this package, by giving access to the main classes ( Map, CurrentGame, Player )
 */
public class Model {

    /**
     * Is the reference to the CurrentGame class that is created here
     */
    private static CurrentGame game;

    /**
     * Default constructor
     *
     * Initialize the whole model
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


    }

    /**
     * @return the CurrentGame instance used for this game
     */
    public static CurrentGame getGame() {
        return game;
    }

    /**
     * Sets the CurrentGame instance used for this game
     * @param game is the instance
     */
    public static void setGame(CurrentGame game) { Model.game = game;}

    /**
     *
     * @return the Map instance used for this game
     */
    public static Map getMap() {
        return getGame().getMap();
    }

    /**
     *
     * @param playerId is the id of the wanted player
     * @return the Player class of the correspondent player
     */
    public static Player getPlayer(int playerId){ return getGame().getPlayers().get(playerId);}
}