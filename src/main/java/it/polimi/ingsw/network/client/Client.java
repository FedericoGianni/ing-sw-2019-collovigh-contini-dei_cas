package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.utils.PlayerColor;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

public abstract class Client {

    private int gameId = -1;
    private int playerId = -1;


    // Connection Handling

    /**
     *
     * @param name is the name chosen for the login
     * @param color is the color chosen for the player
     * @return the id of the player if the login was successful, -1 otherwise
     */
    public abstract int joinGame(String name, PlayerColor color);

    /**
     *
     * @param mapType is the map the player wants to choose
     */
    //public abstract void voteMap(int mapType);


    /**
     * this method will reconnect the player to the server if the client was shut down
     * @param name is the name chosen
     * @return the player id or -1 if something went wrong
     */
    public abstract int reconnect(String name);


    // Getter

    public int getGameId() {

        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setGameId(int gameId) {

        this.gameId = gameId;

    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }


    // start game logic things

    //SPAWN

    /**
     * Forward the PowerUp chosen by the player to discard to spawn
     * @param powerUp powerUp chosen by the user to spawn in the same color spawn cell
     */
    public abstract void spawn(CachedPowerUp powerUp);

    //SHOOT

    /**
     * will be used for both powerUp and Actions
     * @param jsonAction is the action the client submits
     */
    public abstract void doAction(JsonAction jsonAction);

    /**
     * This helper method is used to let the client ask if a single direction is valid, so it doesn't have to
     * know all map adjacences in local, but it just asks the server every time he needs
     * @param row index of the matrix inside the Map
     * @param column index of the column inside the Map
     * @param direction Direction (N,S,E,O) where the player wants to move from his current position
     * @return true if the direction is valid, false otherwise
     */
    public abstract boolean askMoveValid(int row, int column, Directions direction);

}
