package it.polimi.ingsw.model.map;

import it.polimi.ingsw.controller.saveutils.SavedCell;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Subject;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.utils.CellColor;
import it.polimi.ingsw.utils.Directions;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class Cell extends Subject implements Serializable {

    private CellColor color;
    private boolean visit;
    private Point adjNorth;
    private Point adjSouth;
    private Point adjEast;
    private Point adjWest;
    private List<Integer> playersHere;

    /**
     * Default constructor
     */
    public Cell() {
        this.visit=false;
        this.playersHere = new ArrayList<>();
    }

    /**
     * This constructor can be used to initialize the Cell passing some parameters
     * @param color Cell color
     * @param adjNorth a Cell adjacent to the north of the Cell which is going to be constructed, null if there are none
     * @param adjSouth a Cell adjacent to the south of the Cell which is going to be constructed, null if there are none
     * @param adjEast a Cell adjacent to the east of the Cell which is going to be constructed, null if there are none
     * @param adjWest a Cell adjacent to the west of the Cell which is going to be constructed, null if there are none
     */
    public Cell(CellColor color, Point adjNorth, Point adjSouth, Point adjEast, Point adjWest) {
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




    public CellColor getColor() {
        return color;
    }

    public void setAdjNorth(Point adjNorth) {
        this.adjNorth = adjNorth;
    }

    public void setAdjSouth(Point adjSouth) {
        this.adjSouth = adjSouth;
    }

    public void setAdjEast(Point adjEast) {
        this.adjEast = adjEast;
    }

    public void setAdjWest(Point adjWest) {
        this.adjWest = adjWest;
    }

    public void setColor(CellColor color) {
        this.color = color;
    }

    /**
     * THis method will be used in load from save
     * @param cell is the cell from whom copy the parameters
     */
    public void copyParamFrom(Cell cell){

        this.color = cell.color;

        this.adjNorth = cell.adjNorth;
        this.adjSouth = cell.adjSouth;
        this.adjWest = cell.adjWest;
        this.adjEast = cell.adjEast;

        this.playersHere = cell.playersHere;
    }

    /**
     * @return North adjacent Cell of current Cell instance
     */
    public Cell getNorth() {
        return (adjNorth == null) ? null :Model.getMap().getCell(adjNorth.x,adjNorth.y);
    }

    /**
     * @return South adjacent Cell of current Cell instance
     */
    public Cell getSouth() { return (adjSouth == null) ? null :Model.getMap().getCell(adjSouth.x,adjSouth.y); }

    /**
     * @return East adjacent Cell of current Cell instance
     */
    public Cell getEast() {
        return (adjEast == null) ? null : Model.getMap().getCell(adjEast.x,adjEast.y);
    }

    /**
     * @return West adjacent Cell of current Cell instance
     */
    public Cell getWest() {
        return (adjWest == null) ? null : Model.getMap().getCell(adjWest.x,adjWest.y);
    }

    /**
     *
     * @return a list of Players who are currently inside this Cell
     */
    public List<Player> getPlayers(){
        return this.playersHere
                    .stream()
                    .map( playerId -> Model.getPlayer(playerId))
                    .collect(Collectors.toList());
    }

    /**
     *
     * @param p Player to be added inside this cell
     */
    public void addPlayerHere(Player p)
    {
        this.playersHere.add(p.getPlayerId());
    }

    /**
     *
     * @param p Player to be removed from this cell
     */
    public void removePlayerFromHere(Player p)
    {
        this.playersHere.remove(Integer.valueOf(p.getPlayerId()));
    }


    public void setVisited() {
        this.visit=true;
    }

    public void setVisited(boolean b){
        this.visit = b;
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
    public String toString(){
         return "Cella: colore: " + color;
     }

    public abstract Boolean isAmmoCell();

    public void generateAmmoCard(){

        if (this.isAmmoCell()){

            AmmoCell cell = (AmmoCell) this;

            cell.generateAmmoCard();

        }
    }


    // abstract methods

    /**
     *
     * @return the Ammocard placed in the cell if it is an AmmoCell or null otherwise
     */
    public abstract AmmoCard getAmmoPlaced();

    /**
     *
     * @return the only reference to the AmmoCard placed in the cell and sets it to null if it is an AmmoCell or null otherwise
     */
    public abstract AmmoCard pickAmmoPlaced();

    /**
     *
     * @return the list of weapon contained in the cell, that will be empty if the cell has no weapon, or null if the cell is not a spawn cell
     */
    public abstract List<Weapon> getWeapons();

    /**
     *
     * @param w is the weapon reference
     * @param player is the player that wants to buy it
     * @return the weapon's reference if the cell is Spawn or null otherwise
     * @throws NotEnoughAmmoException if the player can not pay for it
     */
    public abstract Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException;

    /**
     * This method will be used to handle save
     * @return a savable version of the cell
     */
    public abstract SavedCell getSaveVersionOfCell();

    /**
     * This method will return the direction in which a given cell is adjacent to this cell
     * @param cell is another cell
     * @return null if the cells are not adjacent or the direction if them are
     */
    public Directions getAdjacencienceDirection(Cell cell){

        if (cell != null) {

            if ( ( this.adjNorth != null ) && (this.adjNorth.equals(Model.getMap().cellToCoord(cell))) )  return Directions.NORTH;

            if ( ( this.adjSouth != null ) && (this.adjSouth.equals(Model.getMap().cellToCoord(cell))) ) return Directions.SOUTH;

            if ( ( this.adjEast != null ) && (this.adjEast.equals(Model.getMap().cellToCoord(cell))) ) return Directions.EAST;

            if ( ( this.adjWest != null ) && (this.adjWest.equals(Model.getMap().cellToCoord(cell))) ) return Directions.WEST;

        }

        return null;
    }

    /**
     * This method do the same as the adj getter but take as input the direction
     * @param direction is the direction of the adj required
     * @return the cell adj to this in the given direction
     */
    public Cell getCellAdj(Directions direction){

        switch (direction){

            case NORTH:

                return getNorth();

            case SOUTH:

                return getSouth();

            case WEST:

                return getWest();

            case EAST:

                return getEast();

            default:

                return null;
        }
    }
}