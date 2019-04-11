package it.polimi.ingsw;

import java.util.List;

/**
 * 
 */
public class SpawnCell extends Cell {

    /**
     * Default constructor
     */
    public SpawnCell() {
        this.visit=false;
    }

    /**
     * attribute useful for canSee method, which need to check if the cell has already been visited or not
     */
    private boolean visit;

    /**
     * 
     */
    private List<Weapon> weapons;

    /**
     * @return a list of Weapons who are avaiable for sell inside this SpawnCell
     */
    @Override
    public List<Weapon> getWeapons() {
        return weapons;
    }


    /**
     * @param w 
     * @return
     */
    public Boolean buy(Weapon w) {
        // TODO implement here
        return null;
    }

}