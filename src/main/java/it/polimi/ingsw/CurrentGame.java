package it.polimi.ingsw;

import java.util.*;
import java.util.Map;

/**
 * 
 */
public class CurrentGame {

    /**
     * Default constructor
     */
    public CurrentGame() {
    }

    /**
     * 
     */
    private CurrentGame singleton;

    /**
     * 
     */
    private List<Player> players;

    /**
     * 
     */
    private java.util.Map currentMap;

    /**
     * 
     */
    private int roundNumber;

    /**
     * 
     */
    private Player currentPlayer;

    /**
     * 
     */
    private PowerUpDeck powerUpDeck;

    /**
     * 
     */
    private WeaponDeck weaponDeck;

    /**
     * 
     */
    private boolean frenzyRound;

    /**
     * 
     */
    private Player frenzyStarter;

    /**
     * 
     */
    private PowerUpDeck thrashPowerUpDeck;









    /**
     * 
     */
    public void getIstance() {
        // TODO implement here
    }

    /**
     * @return
     */
    public PowerUp drawPowerUp() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Weapon drawWeapon() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Boolean isFrenzy() {
        // TODO implement here
        return null;
    }

    /**
     * @param playerId
     */
    public void activateFrenzy(int playerId) {
        // TODO implement here
    }

    /**
     * @param id 
     * @return
     */
    public Player idToPlayer(int id) {
        // TODO implement here
        return null;
    }

    /**
     * @param p 
     * @return
     */
    public int playerToId(Player p) {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public List<Player> getPlayers() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Map getMap() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getRoundNumber() {
        // TODO implement here
        return 0;
    }

    /**
     * @param r
     */
    public void setRoundNumber(int r) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Player getCurrentPlayer() {
        // TODO implement here
        return null;
    }

    /**
     * @param playerId
     */
    public void setCurrentPlayer(int playerId) {
        // TODO implement here
    }

    /**
     * @param p
     */
    public void discardPowerUp(PowerUp p) {
        // TODO implement here
    }

}