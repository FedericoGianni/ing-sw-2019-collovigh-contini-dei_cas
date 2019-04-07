package it.polimi.ingsw;

import java.util.*;
import java.util.Map;

/**
 *
 * This is the main class, and it stores every information regarding the current game.
 * It stores a list of the current Players and their stats like damage taken, weapons, etc. and the actual state of the map like
 * the map type, which depends on the number of players and the actual position of the players in the map.
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
     * @return a randomly picked PowerUp from the current powerUp deck
     */
    public PowerUp drawPowerUp() {
        return powerUpDeck.getRandomCard();
    }

    /**
     * @return a random weapon from the weaponDeck
     */
    public Weapon drawWeapon() {
        return weaponDeck.getRandomCard();
    }

    /**
     * @return true if frenzy round is active, otherwise return false
     */
    public Boolean isFrenzy() {
        return frenzyRound;
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
        //TODO implement here
        return null;
    }

    /**
     * @param p player passed as parameter
     * @return an integer representing the unique id of Player p
     */
    public int playerToId(Player p) {
        return p.getPlayerId();
    }

    /**
     * @return a list which contains current players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the actual Map
     */
    public Map getMap() {
        return currentMap;
    }

    /**
     * @return current round number
     */
    public int getRoundNumber() {
        return roundNumber;
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
        //Todo implement here
    }

}