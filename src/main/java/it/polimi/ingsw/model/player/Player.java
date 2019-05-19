package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.DeadPlayerException;
import it.polimi.ingsw.customsexceptions.OverKilledPlayerException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.weapons.WeaponBag;
import it.polimi.ingsw.view.virtualView.observers.Observer;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class represents the Player
 */
public class Player {

    /**
     * Constructor
     * @param name is a string with the name chosen by the player is also the login identifier
     * @param id is a numeric identifier that also represents the place in the turn
     * @param color is the color of the character chosen by the player
     */
    public Player(String name, int id, PlayerColor color) {

        this.name = name;
        this.id = id;
        this.color = color;

        this.stats = new Stats(null);
        if (Observers.isInitialized()) this.stats.addObserver(Observers.getStatsObserver(id));

        this.currentPowerUps = new PowerUpBag();
        if (Observers.isInitialized()) this.currentPowerUps.addObserver(Observers.getPowerUpBagObserver(id));

        this.ammo = new AmmoBag();
        if (Observers.isInitialized()) this.ammo.addObserver(Observers.getAmmoBagObserver(id));

        this.currentWeapons = new WeaponBag();

    }

    private  String name;
    private int id;
    private PlayerColor color;
    private Stats stats;
    private PowerUpBag currentPowerUps;
    private AmmoBag ammo;
    private WeaponBag currentWeapons;




    /**
     * @return the current position of the player
     */
    public Cell getCurrentPosition() {

        return this.stats.getCurrentPosition();
    }


    /**
     * @param c set the player's position in the map
     */
    public void setPlayerPos(Cell c) {
        if(this.getCurrentPosition()!=null)
            this.getCurrentPosition().removePlayerFromHere(this);
         this.stats.setCurrentPosition(c);
         c.addPlayerHere(this);
    }

    /**
     * @return the player's identifier
     */
    public int getPlayerId() {
        return this.id;
    }


    /**
     * @return the player's name
     */
    public String getPlayerName() {
        return this.name;
    }

    /**
     * @return  a COPY of player's weapon list ( copy of the list but actual elements)
     */
    public List<Weapon> getWeapons() {

        return this.currentWeapons.getList();
    }

    /**
     * @param w is the weapon to add
     */
    public void addWeapon(Weapon w) {

        this.currentWeapons.addItem(w);

    }

    /**
     * @param w is the weapon to delete
     */
    public void delWeapon(Weapon w) throws CardNotPossessedException {

        this.currentWeapons.getItem(w);
    }

    /**
     *
     * @return  currentPlayer PowerUpBag
     */
    public PowerUpBag getPowerUpBag(){
        return currentPowerUps;
    }

    /**
     * @return the player's powerUp list
     */
    public List<PowerUp> getPowerUps() {

        return this.currentPowerUps.getList();
    }

    /**
     * This action will delete one powerUp from Player's inventory and add it's value to the AmmoBag
     *
     * @param powerUp is the power up that needs to be turn into AmmoCubes
     */
    public void sellPowerUp(PowerUp powerUp){

       this.ammo.addItem(this.currentPowerUps.sellItem(powerUp));

    }

    /**
     * @return true if the player has already 3 powerUps
     */
    public Boolean hasMaxPowerUp(Player p) {

        return (p.currentPowerUps.getList().size()==3);
    }

    /**
     *
     * @return true if the player has already 3 weapons
     */
    public Boolean hasMaxWeapons() {

        return (this.currentWeapons.getList().size()>=3);
    }

    /**
     * This method adds a power up to the player's inventory
     *
     * @param p is the powerUp to add
     */
    public void addPowerUp(PowerUp p) {

        this.currentPowerUps.addItem(p);
    }

    public PlayerColor getColor() {
        return color;
    }

    public List<Player> canTarget() {
        return null;
    }//requires weapon implemntation


    public Stats getStats() {
        return stats;
    }

    public WeaponBag getCurrentWeapons(){

        return currentWeapons;
    }

    public List<Player> canSee() {

        Cell c=this.getCurrentPosition();
        Cell d=c;
        List<Player> visibili;
        visibili=c.getPlayers();
        visibili.remove(this);//with these instructions i'm sure to take the players that are in the current cell


        if(c.getNorth() !=null)
        {

            c.setVisited();
            c=c.getNorth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());

            visibili=runner(visibili,c);
            c=d;
        }
        if(c.getEast() !=null && !c.getEast().alreadyVisited() )//devo fare !c.getEast().alreadyvisitd
        {
            c.setVisited();
            c=c.getEast();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
            c=d;
        }
        if(c.getWest() !=null && !c.getWest().alreadyVisited() )
        {
            c.setVisited();
            c=c.getWest();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
            c=d;
        }
        if(c.getSouth() !=null && !c.getSouth().alreadyVisited() )
        {
            c.setVisited();
            c=c.getSouth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
        }
        Model.getMap().setUnvisited();
        return visibili;//handle a nullPointerExcpetion if you can't see any other player

    }
    /**
     * @return a list of the players the current player can see
     */

//useful differentiate because the first check can change the color, after the first one thc eoclor must be all the same
    public List<Player> runner(List<Player> visibili,Cell c)
    {
        if(c.getNorth() !=null && c.getNorth().getColor()==c.getColor() && !c.getNorth().alreadyVisited())//if the color is different you change the room, so you can't see other players
        {
            c.setVisited();
            c=c.getNorth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
        }
        if(c.getEast() !=null && c.getEast().getColor()==c.getColor() && !c.getEast().alreadyVisited())
        {
            c.setVisited();
            c=c.getEast();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
        }
        if(c.getWest() !=null && c.getWest().getColor()==c.getColor() && !c.getWest().alreadyVisited())
        {
            c.setVisited();
            c=c.getWest();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
        }
        if(c.getSouth() !=null && c.getSouth().getColor()==c.getColor() && !c.getSouth().alreadyVisited())
        {
            c.setVisited();
            c=c.getSouth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            visibili=runner(visibili,c);
        }
        return visibili;
    }

    /**
     * @return visible players at this time of the research
     */

    public List<Player> canBeTargetedBy() {
        // TODO implement here
        return null;
    }

    /**
     * @param c is the ammo cube to add
     */
    public void addCube(AmmoCube c) {
        this.ammo.addItem(c);
    }

    /**
     *
     * @return the list of ammo cubes
     */
    public List<AmmoCube> getAmmo(){

        return this.ammo.getList();
    }


    public void pickAmmoHere(){
        AmmoCard a = stats.getCurrentPosition().pickAmmoPlaced();
        //problema il player può decidere quali inserire se non può inserirli tutti
        if(a.getAmmoList().size() > 2){
            List<AmmoCube> l = a.getAmmoList();
            for(AmmoCube ammoCube : l){
                ammo.addItem(ammoCube);
            }
        } else{
            List<AmmoCube> l = a.getAmmoList();
            for(AmmoCube ammoCube : l){
                ammo.addItem(ammoCube);
            }
            drawPowerUp();
        }
    }

    /**
     *
     * @return the AmmoBag
     */
    public AmmoBag getAmmoBag()
    {
        return this.ammo;
    }
    /**
     * This function will subtract an ammo cube of the given
     * @param color and
     * @return it
     */
    public AmmoCube pay(Color color) throws CardNotPossessedException{

        return this.ammo.getItem(this.ammo.getList().stream()
                .filter( ammoCube -> ammoCube.getColor()==color)
                .collect(Collectors.toList())
                .get(0)
        );
    }

    public Weapon buy(Weapon w)
    {return null;}

    /**
     * @return the deaths count of the player
     */
    public int numDeaths() {

        return this.stats.getDeaths();
    }

    /**
     *
     * this function is meant to be used only in Game recovery (EG: load a saved game)
     * @param deaths the deaths count of the player
     */
    public void setDeath(int deaths) {

        this.stats.setDeaths(deaths);
    }


    /**
     * increase the deaths count by 1
     */
    public void incrDeaths() {

        this.stats.addDeath();
    }

    /**
     * @param fromPlayerId is the id of the player who made the damage
     * @param value is the value of the damage
     */
    public void addDmg(int fromPlayerId, int value) throws DeadPlayerException,OverKilledPlayerException{

        this.stats.addDmgTaken(value, fromPlayerId);
    }

    /**
     * This method will reset the HP of the player, will be called by controller after the point count
     */
    public void resetDmg(){

        stats.resetDmg();

    }

    /**
     * @param fromPlayerId is the id of the player who gave the marks
     * @param value is the number of marks
     */
    public void addMarks(int fromPlayerId, int value) {

        for (int i = 0; i < value ; i++) {

            this.stats.addMarks(fromPlayerId);
        }
    }

    /**
     *
     * @return a copy of the marks' list
     */
    public List<Integer> getMarks(){

        return this.getStats().getMarks();
    }

    /**
     *
     * @return a copy of the damage list
     */
    public List<Integer> getDmg(){

        return this.getStats().getDmgTaken();
    }


    /**
     * @param value is the score to be added
     */
    public void addScore(int value) {

        this.stats.addScore(value);
    }

    public int distance(Player shooter,Player target)
    {
        // TODO implementation here
        return 0;
    }

    /**
     * Draw a PowerUp from model and assign it to the player who called this function
     */
    public void drawPowerUp() {
        this.getPowerUpBag().addItem(Model.getGame().drawPowerUp());
    }



}