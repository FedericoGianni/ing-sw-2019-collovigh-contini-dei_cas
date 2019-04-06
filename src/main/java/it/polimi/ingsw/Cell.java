package it.polimi.ingsw;

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
    private CellColor color;

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
     * @return
     */
   /* public enum getColor() {
        // TODO implement here
        return null;
    }*/

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