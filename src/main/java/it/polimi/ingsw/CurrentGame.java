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
     * Constructor
     *
     * maybe can take the attributes from the Model Class TBD
     *
     * @param players ordered list of the players, the order represent the ID
     * @param currentMap pointer to the Map used in the game
     * @param powerUpDeck pointer to the "good" powerUp deck
     * @param weaponDeck pointer to the weapon deck
     * @param frenzyRound boolean which is set to tue only if frenzy round has started
     * @param frenzyStarter player who has taken the last skull from the kill shot track
     * @param firstPlayer WHY ???  TBD
     * @param thrashPowerUpDeck deck of discarded powerUps
     */
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


    /**
     *
     * @return CurrentGame. only one instance of CurrentGame can exist at one time, if the actual instance is set to null
     * it creates a new instance of this Class, if it already exists it just return current instance
     *
     *
     *  HF <------- note me ----------> TBD
     */
    public static CurrentGame getIstance(List<Player> players, Map currentMap, PowerUpDeck powerUpDeck, WeaponDeck weaponDeck, boolean frenzyRound, Player frenzyStarter, Player firstPlayer, PowerUpDeck thrashPowerUpDeck) {
        if(singleton == null)
            singleton = new CurrentGame(players, currentMap, powerUpDeck, weaponDeck, frenzyRound, frenzyStarter, firstPlayer, thrashPowerUpDeck);
        return singleton;
    }

    /**
     *  Temporary, hopefully will get canceled TBD
     *
     */
    public static CurrentGame getInstance() {

        if (singleton == null){

            singleton= new CurrentGame(null,null,null,null,false,null,null,null);
        }


        return singleton;

    }



    /**
     * @return a randomly picked PowerUp from the current powerUp deck if this is empty it sets the trash one as the ggod one
     */
    public PowerUp drawPowerUp() {

        PowerUp powerUp = powerUpDeck.getRandomCard();

        if (powerUp == null){

            this.powerUpDeck = this.thrashPowerUpDeck;

            this.thrashPowerUpDeck = new PowerUpDeck();

            powerUp = powerUpDeck.getRandomCard();
        }

        return powerUp;
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
     * This method enable frenzy mode no the current game
     *
     * @param playerId is the id of the player who takes the last skull from the KillShotTrack (it will be the last player to play)
     */
    public void activateFrenzy(int playerId) {

        this.frenzyRound = true;

        this.frenzyStarter = CurrentGame.idToPlayer(playerId);

    }

    /**
     *
     * @return the last Player to play
     */
    public Player getFrenzyStarter() {
        return frenzyStarter;
    }

    /**
     * This method is a util method that will be used to translate the player ID in the pointer to that player
     *
     * will be called this way: CurrentGame.idToPlayer() so that it can be used also in other classes easily
     *
     * @param id is the identifier of the player which is individual for each player and can not be changed during the game
     * @return a pointer to the aforementioned player
     */
    public static Player idToPlayer(int id) {

        List<Player> list = CurrentGame.getInstance().getPlayers();


        return list.get(id);
    }

    /**
     * @param p player passed as parameter
     * @return an integer representing the unique id of Player p
     */
    public static int playerToId(Player p) {
        return p.getPlayerId(p);
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
     * this method will set the player whose ID is in the method as the current Player
     *
     * @param playerId is the identifier of the player
     */
    public void setCurrentPlayer(int playerId) {
        currentPlayer = CurrentGame.idToPlayer(playerId);
    }

    /**
     * @param p is a power Up that is meant to be discarded
     */
    public void discardPowerUp(PowerUp p) {


        thrashPowerUpDeck.reinsert(p);

    }

}