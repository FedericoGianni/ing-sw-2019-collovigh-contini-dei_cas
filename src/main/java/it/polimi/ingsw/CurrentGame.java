package it.polimi.ingsw;

import java.util.*;
import java.util.Map;

/**
 * 
 */
public class CurrentGame {

    private CurrentGame(List<Player> players, Map currentMap, PowerUpDeck powerUpDeck, WeaponDeck weaponDeck, boolean frenzyRound, Player frenzyStarter, Player firstPlayer, PowerUpDeck thrashPowerUpDeck) {
        this.players = players;
        this.currentMap = currentMap;
        this.roundNumber = 0;
        this.currentPlayer = firstPlayer;
        this.powerUpDeck =  powerUpDeck;
        this.weaponDeck = weaponDeck;
        this.frenzyRound = frenzyRound;
        this.frenzyStarter = frenzyStarter;
        this.firstPlayer = firstPlayer;
        this.thrashPowerUpDeck = thrashPowerUpDeck;
    }


    private static CurrentGame singleton;
    private List<Player> players;
    private java.util.Map currentMap;
    private int roundNumber;
    private Player currentPlayer;
    private PowerUpDeck powerUpDeck;
    private WeaponDeck weaponDeck;
    private boolean frenzyRound;
    private Player frenzyStarter;
    private Player firstPlayer;
    private PowerUpDeck thrashPowerUpDeck;


    /**
     *
     * @return CurrentGame. only one instance of CurrentGame can exist at one time, if the actual instance is set to null
     * it creates a new instance of this Class, if it already exists it just return current instance
     */
    public CurrentGame getIstance(List<Player> players, Map currentMap, PowerUpDeck powerUpDeck, WeaponDeck weaponDeck, boolean frenzyRound, Player frenzyStarter, Player firstPlayer, PowerUpDeck thrashPowerUpDeck) {
        if(singleton == null)
            singleton = new CurrentGame(players, currentMap, powerUpDeck, weaponDeck, frenzyRound, frenzyStarter, firstPlayer, thrashPowerUpDeck);
        return singleton;
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
        return p.getPlayerId();
    }

    /**
     * @return
     */
    public List<Player> getPlayers() {
        // TODO implement here
        return null;
    }

    /**
     * @return the actual Map
     */
    public Map getMap() {
        return currentMap;
    }

    /**
     * @return
     */
    public int getRoundNumber() {
        // TODO implement here
        return 0;
    }

    /**
     * @param r represent the round number to be set
     */
    public void setRoundNumber(int r) {
        roundNumber = r;
    }

    /**
     * @return the id of current active Player
     */
    public int getCurrentPlayer() {
        return this.currentPlayer.getPlayerId();
    }

    /**
     * @param playerId
     */
    public void setCurrentPlayer(int playerId) {
        currentPlayer = idToPlayer(playerId);
    }

    /**
     * @param p
     */
    public void discardPowerUp(PowerUp p) {
        powerUpDeck
    }

}