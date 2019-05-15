package it.polimi.ingsw.network;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

public abstract class Client {

    private int gameId = -1;
    private int playerId = -1;


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
    public abstract void voteMap(int mapType);

    /**
     * this method will reconnect the player to the server if the client still run
     */
    public abstract void reconnect();

    /**
     * this method will reconnect the player to the server if the client was shut down
     * @param name is the name chosen
     * @return the player id or -1 if something went wrong
     */
    public abstract int reconnect(String name);

    /**
     *
     * @return the current GameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     *
     * @return the player Id in the current Game
     */
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



    public abstract void spawn(CachedPowerUp powerUp);
}
