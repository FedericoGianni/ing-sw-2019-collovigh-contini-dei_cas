package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class Cell {

    /**
     * Default constructor
     */
    public Cell() {
        this.visit=false;
    }

    /**
     * This constructor can be used to initialize the Cell passing some parameters
     * @param color Cell color
     * @param adjNorth a Cell adjacent to the north of the Cell which is going to be constructed, null if there are none
     * @param adjSouth a Cell adjacent to the south of the Cell which is going to be constructed, null if there are none
     * @param adjEast a Cell adjacent to the east of the Cell which is going to be constructed, null if there are none
     * @param adjWest a Cell adjacent to the west of the Cell which is going to be constructed, null if there are none
     */
    public Cell(CellColor color, Cell adjNorth, Cell adjSouth, Cell adjEast, Cell adjWest) {
        this.color = color;
        this.adjNorth = adjNorth;
        this.adjSouth = adjSouth;
        this.adjEast = adjEast;
        this.adjWest = adjWest;
        this.playersHere = null;
        this.visit = false;
    }

    /**
     * This constructor is used to return a copy of the Cell passed as a parameter
     * @param clone Cell to be cloned
     */
    public Cell(Cell clone){
        this.color = clone.color;
        this.adjNorth = clone.adjNorth;
        this.adjSouth = clone.adjSouth;
        this.adjEast = clone.adjEast;
        this.adjWest = clone.adjWest;
        this.playersHere = clone.playersHere;
        this.visit = clone.visit;
    }

    private CellColor color;
    private boolean visit;
    private Cell adjNorth;
    private Cell adjSouth;
    private Cell adjEast;
    private Cell adjWest;
    private List<Player> playersHere=new ArrayList<>();


    public CellColor getColor() {
        return color;
    }

    public void setAdjNorth(Cell adjNorth) {
        this.adjNorth = adjNorth;
    }

    public void setAdjSouth(Cell adjSouth) {
        this.adjSouth = adjSouth;
    }

    public void setAdjEast(Cell adjEast) {
        this.adjEast = adjEast;
    }

    public void setAdjWest(Cell adjWest) {
        this.adjWest = adjWest;
    }

    public void setColor(CellColor color) {
        this.color = color;
    }

    /**
     * @return North adjacent Cell of current Cell instance
     */
    public Cell getNorth() {
        return this.adjNorth;
    }

    /**
     * @return South adjacent Cell of current Cell instance
     */
    public Cell getSouth() {
        return this.adjSouth;
    }

    /**
     * @return East adjacent Cell of current Cell instance
     */
    public Cell getEast() {
        return this.adjEast;
    }

    /**
     * @return West adjacent Cell of current Cell instance
     */
    public Cell getWest() {
        return this.adjWest;
    }

    /**
     *
     * @return
     */
    public List<Weapon> getWeapons(){
        return null;
    }

    /**
     *
     * @return null since this is the abstract class and SpawnCell doesn't have Ammo inside
     * the method is instead overrided in AmmoCell method so that it returns the Ammo placed
     */
    public AmmoCard getAmmoPlaced(){
        return null;
    }

    /**
     *
     * @return ull since this is the abstract class and SpawnCell doesn't have Ammo inside
     * the method is instead overrided in AmmoCell method so that it returns the Ammo placed to be
     * taken by the player who calls this method
     */
    public AmmoCard pickAmmoPlaced(){
        return null;
    }

    /**
     *
     * @return a list of Players who are currently inside this Cell
     */
    public List<Player> getPlayers()
    {
        return this.playersHere;
    }

    /**
     *
     * @param p Player to be added inside this cell
     */
    public void addPlayerHere(Player p)
    {
        this.playersHere.add(p);
    }

    /**
     *
     * @param p Player to be removed from this cell
     */
    public void removePlayerFromHere(Player p)
    {
        this.playersHere.remove(p);
    }


    public void setVisited() {
        this.visit=true;
    }

    public void unvisit() {
        this.visit=false;
    }

    /**
     * Method useful for canSee method inside Player class
     * @return true if the Cell has been already visited by the canSee algorithm, false otherwise
     */
    public boolean alreadyVisited() {
        return this.visit;
    }

    @Override
    public String toString() {


        return this.getClass().toString() + Model.getMap().cellToCoord(this);
    }
}