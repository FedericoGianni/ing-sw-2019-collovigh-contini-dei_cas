package it.polimi.ingsw;

import java.util.List;

/**
 * 
 */
public class Cell {

    /**
     * Default constructor
     */
    public Cell() {
    }

    /**
     * 
     */
   // private enum color;

    /**
     * 
     */
    private AmmoCard ammoPlaced;

    /**
     * 
     */
    private Cell adjNorth;

    /**
     * 
     */
    private Cell adjSouth;

    /**
     * 
     */
    private Cell adjEast;

    /**
     * 
     */
    private Cell adjWest;

    int color; //like color areas,the rooms of the map
    private List<Player> playersHere;

    public List<Player> getPlayers()
    {
        return this.playersHere;
    }

    public void setPlayersHere(Player p) {
        this.playersHere.add(p);
    }

    public void playerMoves(Player p)
    {
        this.playersHere.remove(p);
    }
    /**
     * @return
     */
   /* public enum getColor() {
        // TODO implement here
        return null;
    }*/
    public int getColor()
    {
        return this.color;
    }
    public void setColor(int n)
    {
        this.color=n;
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
     * @return
     */
    public Cell getNorth() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Cell getSouth() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Cell getEast() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public Cell getWest() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */


}