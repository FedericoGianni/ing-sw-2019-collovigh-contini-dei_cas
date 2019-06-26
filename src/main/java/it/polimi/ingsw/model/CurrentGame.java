package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Map;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Skull;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.powerup.PowerUpDeck;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 *
 * This is the main class, and it stores every information regarding the current game.
 * It stores a list of the current Players and their stats like damage taken, weapons, etc. and the actual state of the map like
 * the map type, which depends on the number of players and the actual position of the players in the map.
 *
 */

public class CurrentGame extends Subject{


    private final int skulls ;
    private List<Player> players;
    private Map currentMap;
    private PowerUpDeck powerUpDeck;
    private WeaponDeck weaponDeck;
    private PowerUpDeck thrashPowerUpDeck;
    private List<Skull> killShotTrack;


    public CurrentGame(List<Player> players, Map currentMap, int skulls) {

        Collections.sort(players, Comparator.comparing(Player::getPlayerId));

        this.players = players;
        this.currentMap = currentMap;
        this.powerUpDeck =  PowerUpDeck.populatedDeck();
        this.weaponDeck = WeaponDeck.populateDeck();
        this.thrashPowerUpDeck = new PowerUpDeck();
        this.killShotTrack = new ArrayList<>();
        this.skulls = skulls;
    }

    /**
     * This constructor is used to create a copy of the CurrentGame instance passed as parameter
     * @param clone CurrentGame instance to be copied
     */
    public CurrentGame(CurrentGame clone){

        this.players = new ArrayList<>();
        this.players.addAll(clone.players);

        this.currentMap = clone.currentMap;
        this.powerUpDeck =  new PowerUpDeck(clone.powerUpDeck);
        this.weaponDeck = new WeaponDeck(clone.weaponDeck);
        this.thrashPowerUpDeck = new PowerUpDeck(clone.thrashPowerUpDeck);
        this.skulls = clone.skulls;
        this.killShotTrack = new ArrayList<>(clone.killShotTrack);
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
    public Player idToPlayer(int id) {

        List<Player> list = this.getPlayers();
        return list.get(id);
    }

    /**
     * @param p player passed as parameter
     * @return an integer representing the unique id of Player p
     */
    public int playerToId(Player p) {
            return this.getPlayers().indexOf(p);
    }

    /**
     * @return a list which contains a copy of current players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    public List<Skull> getKillShotTrack() {
        return killShotTrack;
    }

    public void setKillShotTrack(List<Skull> killShotTrack) {
        this.killShotTrack = killShotTrack;
        updateAll(this);
    }
    
    /**
     * This method is used with multiple kills in one turn
     * @param killerId is the id of the player who shoot
     * @param overkill is a list of Boolean that are true if the player gets overkilled
     */
    public void addkills(int killerId, List<Boolean> overkill) {

        for (Boolean b: overkill){

            this.killShotTrack.add(new Skull(killerId,b));

        }

        updateAll(this);
    }

    /**
     * This method is used with multiple kills in one turn
     * @param killerId is the id of the player who shoot
     * @param overkill is a list of Boolean that are true if the player gets overkilled
     */
    public void addkills(int killerId, Boolean overkill) {

        this.killShotTrack.add(new Skull(killerId,overkill));

        updateAll(this);
    }

    public int getSkulls() {
        return skulls;
    }

    /**
     * @return the actual Map
     */
    public Map getMap() {
        return this.currentMap;
    }

    /**
     * @param p
     */
    public void discardPowerUp(PowerUp p) {
        thrashPowerUpDeck.reinsert(p);
    }

}