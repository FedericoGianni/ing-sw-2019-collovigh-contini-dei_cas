package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.PlayerColor;
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

        this.stats = new Stats();
        if (Observers.isInitialized()) this.stats.addObserver(Observers.getStatsObserver(id));

        this.currentPowerUps = new PowerUpBag();
        if (Observers.isInitialized()) this.currentPowerUps.addObserver(Observers.getPowerUpBagObserver(id));

        this.ammo = new AmmoBag(this);
        if (Observers.isInitialized()) this.ammo.addObserver(Observers.getAmmoBagObserver(id));

        this.currentWeapons = new WeaponBag();
        if (Observers.isInitialized()) this.currentWeapons.addObserver(Observers.getWeaponBagObservers(id));

    }

    /**
     * name of the player
     */
    private String name;
    /**
     * id of the player
     */
    private int id;
    /**
     * color of the player
     */
    private PlayerColor color;
    /**
     * class containing all the stats of the player
     * @see Stats
     */
    private Stats stats;
    /**
     * class containing the player powerUps
     * @see PowerUpBag
     */
    private PowerUpBag currentPowerUps;
    /**
     * class containing all the player Ammp
     * @see AmmoBag
     */
    private AmmoBag ammo;
    /**
     * Class containing the player weapons
     * @see WeaponBag
     */
    private WeaponBag currentWeapons;




    /**
     * @return the current position of the player
     */
    public Cell getCurrentPosition() {

        return this.stats.getCurrentPosition();
    }

    public Cell getCurrentPositionCopy()
    {
        return this.stats.getCurrentPositionCopy();
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
     * This action will delete one powerUp from Player's inventory and add it's value to the AmmoBag
     *
     * @param powerUp is the power up that needs to be turn into AmmoCubes
     *
     * @return the AmmoCube the player has gained
     */
    public AmmoCube sellPowerUp(PowerUp powerUp){

        AmmoCube revenue = this.currentPowerUps.sellItem(powerUp);

       this.ammo.addItem(revenue);

        return revenue;
    }

    /**
     * setter for stats will be used only for loading a saved game
     * @param stats is the stats Class
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * used for the copies
     * @param c is a cell
     */
    public void setPlayerPosCopy(Cell c)
    {
        this.stats.setCurrentPositionCopy(c);
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

    public Stats getStats() {
        return stats;
    }

    public WeaponBag getCurrentWeapons(){

        return currentWeapons;
    }

    /**
     *
     * @return the list of players this can see
     */
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

    //useful differentiate because the first check can change the color, after the first one thc eoclor must be all the same

    /**
     * Called by canSee() for cycling through cells
     * @param visibili player visibili
     * @param c is a cell
     * @return a list of players
     */
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
     * pick the ammo from the current cell
     */
    public void pickAmmoHere(){

        if (stats.getCurrentPosition().isAmmoCell()) {

            AmmoCard ammoCard = stats.getCurrentPosition().pickAmmoPlaced();

            //if the ammo card has a powerUp the player draws a powerUp

            if (ammoCard.getPowerUp()) {

                drawPowerUp();

            }

            // adds the ammoCube

            for (AmmoCube ammoCube : ammoCard.getAmmoList()) {

                ammo.addItem(ammoCube);

            }

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
     * This function will subtract from the AmmoBag the cost given
     * @param cost is the cost to pay
     */
    public void pay(List<AmmoCube> cost) throws CardNotPossessedException{

        // if the player can not pay throw an exception

        if (!this.canPay(cost)) throw new CardNotPossessedException();

        // gets the list of the color to pay

        List<Color> required = cost
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        // fo each element of the above list

        for (Color ammocube : required) {

            // gets the cubes that are possessed which match the required color

            List<AmmoCube> matchingPossessed = this
                    .ammo
                    .getList()
                    .stream()
                    .filter( ammoCube -> ammoCube.getColor().equals(ammocube) )
                    .collect(Collectors.toList());

            if (matchingPossessed.isEmpty()) throw new CardNotPossessedException();

            // delete the first one

            this.ammo.getItem(matchingPossessed.get(0));

        }
    }

    /**
     * This function will subtract from the AmmoBag the cost given
     * @param cost is the cost to pay
     */
    public void pay(Color cost) throws CardNotPossessedException{

        // gets the cubes that are possessed which match the required color

        List<AmmoCube> matchingPossessed = this
                .ammo
                .getList()
                .stream()
                .filter( ammoCube -> ammoCube.getColor().equals(cost) )
                .collect(Collectors.toList());

        // if the list is empty the player can not pay for it

        if (matchingPossessed.isEmpty()) throw new CardNotPossessedException();

        // delete the first one

        this.ammo.getItem(matchingPossessed.get(0));
    }

    /**
     * This method tries to buy the specified weapon
     * @param w is the weapon the player wants to buy
     * @throws NotEnoughAmmoException if the player can not afford to buy it
     */
    public void buy(Weapon w) throws NotEnoughAmmoException{

        try {

            // adds the item from the cell and deletes it from it

            this.currentWeapons.addItem(this.stats.getCurrentPosition().buy(w, this));

            // pay the cost of the weapon

            this.pay(w.getCost());

        }catch (CardNotPossessedException e){

            throw new NotEnoughAmmoException();
        }

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
    public void incrDeaths() {

        this.stats.addDeath();
    }

    /**
     * @param fromPlayerId is the id of the player who made the damage
     * @param value is the value of the damage
     */
    public void addDmg(int fromPlayerId, int value) {

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

    /**
     * Draw a PowerUp from model and assign it to the player who called this function
     */
    public void drawPowerUp() {

        this.getPowerUpBag().addItem(Model.getGame().drawPowerUp());

    }

    /**
     *
     * @param cost is a List of ammoCube that the player is required to pay
     * @return true if the player can pay the cost
     */
    public Boolean canPay(List<AmmoCube> cost) {

        List<Color> possessed = this.ammo
                .getList()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        // gets the list of the color to pay

        List<Color> required = cost
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        // fo each element of the above list

        for (Color ammocube : required) {

            // gets the cubes that are possessed which match the required color

            List<Color> matchingPossessed = possessed
                    .stream()
                    .filter( x -> x.equals(ammocube))
                    .collect(Collectors.toList());

            if (matchingPossessed.isEmpty()) return false;

            possessed.remove(matchingPossessed.get(0));

        }

        return true;
    }


}