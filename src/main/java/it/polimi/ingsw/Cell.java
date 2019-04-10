package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Cell {

    /**
     * Default constructor
     */
    public Cell() {
        this.visit=false;
        ammoPlaced = AmmoCard.generateRandCard();
    }


    public Cell(CellColor color, Cell adjNorth, Cell adjSouth, Cell adjEast, Cell adjWest) {
        this.color = color;
        this.adjNorth = adjNorth;
        this.adjSouth = adjSouth;
        this.adjEast = adjEast;
        this.adjWest = adjWest;
        this.playersHere=null;
        this.visit=false;
        ammoPlaced = AmmoCard.generateRandCard();
    }

    private CellColor color;

    private AmmoCard ammoPlaced;
    private boolean visit;
    private Cell adjNorth;
    private Cell adjSouth;
    private Cell adjEast;
    private Cell adjWest;
    private List<Player> playersHere=new ArrayList<Player>();


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


    public List<Player> getPlayers()
    {
        return this.playersHere;
    }

    public void addPlayerHere(Player p)
    {
        this.playersHere.add(p);
    }

    public void removePlayerFromHere(Player p)
    {
        this.playersHere.remove(p);
    }


    public void setVisited(){
        this.visit=true;
    }

    public boolean alreadyVisited(){
        return this.visit;
    }

    public void unvisit()
    {
        this.visit=false;
    }

    /**
     * @return a randomly generated Ammo Card, based on the probability of the real on-board game deck
     * the ammoPlaced is generated randomly inside the creator so that the ammo is the same for all players and is not
     * generated randomly every time a Player wants to check what Ammo is placed inside this Cell
     */
    public AmmoCard getAmmoPlaced() {

        return ammoPlaced;
    }

    /**
     * @return the Ammo picked up by Player inside this Cell, also it generates a new PowerUp to be placed inside Cell
     */
    public AmmoCard pickAmmoPlaced() {

        AmmoCard tempAmmo = ammoPlaced;
        ammoPlaced = AmmoCard.generateRandCard();

        return tempAmmo;
    }


}