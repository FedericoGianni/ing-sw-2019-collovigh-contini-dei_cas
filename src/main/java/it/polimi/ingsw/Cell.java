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

    public CellColor getColor() {
        return color;
    }

    public Cell(CellColor color, Cell adjNorth, Cell adjSouth, Cell adjEast, Cell adjWest) {
        this.color = color;
        this.adjNorth = adjNorth;
        this.adjSouth = adjSouth;
        this.adjEast = adjEast;
        this.adjWest = adjWest;
    }


    private CellColor color;

    private AmmoCard ammoPlaced;

    private Cell adjNorth;
    private Cell adjSouth;
    private Cell adjEast;
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
}