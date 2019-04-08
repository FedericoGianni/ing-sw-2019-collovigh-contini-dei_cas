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
    }

    public CellColor getColor() {
        return color;
    }

    public Cell(CellColor color, Cell adjNorth, Cell adjSouth, Cell adjEast, Cell adjWest) {
        this.color = color;
        this.adjNorth = adjNorth;
        this.adjSouth = adjSouth;
        this.adjEast = adjEast;
        this.adjWest = adjWest;
        this.playersHere=null;
        this.visit=false;
    }


    private CellColor color;

    private AmmoCard ammoPlaced;
    private boolean visit;
    private Cell adjNorth;
    private Cell adjSouth;
    private Cell adjEast;
    private Cell adjWest;
    private List<Player> playersHere=new ArrayList<Player>();

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
    /**
     * @return
     */
    public AmmoCard getAmmoPlaced() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public AmmoCard pickAmmoPlaced() {
        // TODO implement here
        return null;
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

}