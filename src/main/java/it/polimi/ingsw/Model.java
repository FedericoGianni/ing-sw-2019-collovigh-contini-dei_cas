package it.polimi.ingsw;

import java.util.List;

/**
 * 
 */
public class Model {

    /**
     * Default constructor
     */
    public Model() {
    }

    /**
     *
     */
    private CurrentGame game;

    /**
     *
     * @param mapType
     * @param skulls
     * @param players
     * @return
     */
    public Boolean Initialize(int mapType, int skulls, List<String> players) {

        return null;
    }

    /**
     * @param playerId 
     * @param v
     */
    public void addPoints(int playerId, int v) {
        // TODO implement here
    }

    /**
     * @param toPlayerId 
     * @param fromPlayerId 
     * @param value
     */
    public void addDmg(int toPlayerId, int fromPlayerId, int value) {
        // TODO implement here
    }

    /**
     * @param playerId 
     * @param pos
     */
    /*public void move(int playerId, Pair pos) {
        // TODO implement here
    }*/

    /**
     * @param playerId 
     * @param w
     */
    public void draw(int playerId, Weapon w) {
        // TODO implement here
    }

    /**
     * @param playerId 
     * @param p
     */
    public void draw(int playerId, PowerUp p) {
        // TODO implement here
    }

    /**
     * @param playerId 
     * @param w
     */
    public void discard(int playerId, Weapon w) {
        // TODO implement here
    }

    /**
     * @param playerId 
     * @param p
     */
    public void discard(int playerId, PowerUp p) {
        // TODO implement here
    }

    /**
     * @param playerId 
     * @param v
     */
    public void setPoints(int playerId, int v) {
        // TODO implement here
    }

    /**
     * @param toPlayerId 
     * @param fromPlayerId 
     * @param value
     */
    public void setDmg(int toPlayerId, int fromPlayerId, int value) {
        // TODO implement here
    }

    /**
     * @param playerId
     */
    public void setCurrentPlayer(int playerId) {
        // TODO implement here
    }

    /**
     * @param playerId
     */
    public void activateFrenzy(int playerId) {
        // TODO implement here
    }

}