package it.polimi.ingsw;

import customsexceptions.NullSingletonException;

import java.util.List;


/**
 *
 * This is the main class, and it stores every information regarding the current game.
 * It stores a list of the current Players and their stats like damage taken, weapons, etc. and the actual state of the map like
 * the map type, which depends on the number of players and the actual position of the players in the map.
 *
 */

public class CurrentGame {

    private CurrentGame(List<Player> players, Map currentMap) {
        this.players = players;
        this.currentMap = currentMap;
        this.roundNumber = 0;
        this.powerUpDeck =  PowerUpDeck.populatedDeck();
        this.weaponDeck = new WeaponDeck();
        this.thrashPowerUpDeck = new PowerUpDeck();
    }


    private static CurrentGame singleton = null;
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
    public static CurrentGame getInstance() throws NullSingletonException{
            if (singleton == null) {
                throw new NullSingletonException();
            }
            return singleton;
    }

    public static CurrentGame generateInstance(List<Player> players, Map currentMap){
        singleton = new CurrentGame(players, currentMap);
        return singleton;
    }

    /**
     * @return a randomly picked PowerUp from the current powerUp deck if this is empty it sets the trash one as the good one
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
     * This method is a util method that will be used to translate the player ID in the pointer to that player
     *
     * will be called this way: CurrentGame.idToPlayer() so that it can be used also in other classes easily
     *
     * @param id is the identifier of the player which is individual for each player and can not be changed during the game
     * @return a pointer to the aforementioned player
     */
    public static Player idToPlayer(int id) {
            try {
                List<Player> list = CurrentGame.getInstance().getPlayers();
                return list.get(id);
            }catch(NullSingletonException e){
                e.printStackTrace();
                return null;
            }
    }

    /**
     * @param p player passed as parameter
     * @return an integer representing the unique id of Player p
     */
    public static int playerToId(Player p) {
        try{
            return CurrentGame.getInstance().getPlayers().indexOf(p);
        }catch(NullSingletonException e){
            e.printStackTrace();
        }

        return -1;
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
        thrashPowerUpDeck.reinsert(p);
    }

}