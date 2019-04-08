package it.polimi.ingsw;

import java.util.List;


/**
 *
 * This is the main class, and it stores every information regarding the current game.
 * It stores a list of the current Players and their stats like damage taken, weapons, etc. and the actual state of the map like
 * the map type, which depends on the number of players and the actual position of the players in the map.
 *
 */

public class CurrentGame {

    public CurrentGame(List<Player> players, Map currentMap) {
        this.players = players;
        this.currentMap = currentMap;
        this.roundNumber = 0;
        this.powerUpDeck =  PowerUpDeck.populatedDeck();
        this.weaponDeck = new WeaponDeck();
        this.thrashPowerUpDeck = new PowerUpDeck();
    }


    private static CurrentGame singleton;
    private List<Player> players;
    private Map currentMap;
    private int roundNumber;
    private PowerUpDeck powerUpDeck;
    private WeaponDeck weaponDeck;
    private PowerUpDeck thrashPowerUpDeck;


    /**
     *
     * @return CurrentGame. only one instance of CurrentGame can exist at one time, if the actual instance is set to null
     * it creates a new instance of this Class, if it already exists it just return current instance
     */
    public CurrentGame getIstance(List<Player> players, Map currentMap) {
        if(singleton == null)
            singleton = new CurrentGame(players, currentMap);
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
     * @param p
     */
    public void discardPowerUp(PowerUp p) {
        powerUpDeck.reinsert(p);
    }

}