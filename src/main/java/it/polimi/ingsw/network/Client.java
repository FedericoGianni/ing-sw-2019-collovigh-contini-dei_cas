package it.polimi.ingsw.network;

import it.polimi.ingsw.model.player.PlayerColor;

public abstract class Client {

    private int gameId;
    private int playerId;


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
}
