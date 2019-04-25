package it.polimi.ingsw.model;

import customsexceptions.DeadPlayerException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This Class represents the Player
 */
public class Player {

    /**
     * Default constructor
     */
    public Player(){

        super();
    }

    /**
     * Real constructor
     * @param name is a string with the name chosen by the player is also the login identifier
     * @param id is a numeric identifier that also represents the place in the turn
     * @param color is the color of the character chosen by the player
     */
    public Player(String name, int id, PlayerColor color) {

        this.name = name;
        this.id = id;
        this.color = color;

        this.stats = new Stats(null);
        this.currentPowerUps = new PowerUpBag();
        this.ammo = new AmmoBag();

    }



    /**
     * IDK
     */
    public Player(String nome,Cell posizione){//just for test purpose
        this.name=nome;
        this.stats = new Stats(posizione);
        posizione.addPlayerHere(this);
        this.currentPowerUps = new PowerUpBag();
        this.ammo = new AmmoBag();
    }


    private  String name;
    private int id;
    private PlayerColor color;
    private Stats stats;
    private PowerUpBag currentPowerUps;
    private AmmoBag ammo;




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

         this.stats.setCurrentPosition(c);
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
     * @return the player's weapon list
     */
    public List<NormalWeapon> getWeapon() {

        return null;  //TODO
    }

    /**
     * @param w is the weapon to add
     */
    public void addWeapon(NormalWeapon w) {
        // TODO

    }

    /**
     * @param w is the weapon to delete
     */
    public void delWeapon(NormalWeapon w) {

        //TODO
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
     * @param p
     * @return
     */
    public Boolean hasMaxWeapons() {
        //TODO

        return false;
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

    public List<Player> canSee() {
        Cell c=this.getCurrentPosition();
        List<Player> visibili;
        visibili=c.getPlayers();
        visibili.remove(this);//with these instructions i'm sure to take the players that are in the current cell


        if(c.getNorth() !=null)
        {
            c=c.getNorth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getEast() !=null && !c.alreadyVisited())
        {
            c=c.getEast();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getWest() !=null && !c.alreadyVisited())
        {
            c=c.getWest();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getSouth() !=null && !c.alreadyVisited())
        {
            c=c.getSouth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }


        return visibili;//handle a nullPointerExcpetion if you can't see any other player
        //call a map.setUnvisited
    }
    /**
     * @return a list of the players the current player can see
     */

//useful differentiate because the first check can change the color, after the first one thc eoclor must be all the same
    public List<Player> runner(List<Player> visibili,Cell c)
    {
        if(c.getNorth() !=null && c.getNorth().getColor()==c.getColor() && !c.alreadyVisited())//if the color is different you change the room, so you can't see other players
        {
            c=c.getNorth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getEast() !=null && c.getEast().getColor()==c.getColor() && !c.alreadyVisited())
        {
            c=c.getEast();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getWest() !=null && c.getWest().getColor()==c.getColor() && !c.alreadyVisited())
        {
            c=c.getWest();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
            visibili=runner(visibili,c);
        }
        if(c.getSouth() !=null && c.getSouth().getColor()==c.getColor() && !c.alreadyVisited())
        {
            c=c.getSouth();
            if(c.getPlayers()!=null)
                visibili.addAll(c.getPlayers());
            c.setVisited();
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

    /**
     * This function will subtract an ammo cube of the given
     * @param color and
     * @return it
     */
    public AmmoCube pay(Color color){

        return this.ammo.getItem(this.ammo.getList().stream()
                .filter( ammoCube -> ammoCube.getColor()==color)
                .collect(Collectors.toList())
                .get(0)
        );

        
    }

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
    public void incrDeaths( ) {

        this.stats.addDeath();
    }

    /**
     * @param fromPlayerId is the id of the player who made the damage
     * @param value is the value of the damage
     */
    public void addDmg(int fromPlayerId, int value) throws DeadPlayerException {

        this.stats.addDmgTaken(value, fromPlayerId);
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

}