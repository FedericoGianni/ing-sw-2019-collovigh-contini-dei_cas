package it.polimi.ingsw.model;

import java.util.ArrayList;
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
     * This constructor is used to return a copy of the SpawnCell passed as a parameter
     * @param clone SpawnCell to be cloned
     */
    public SpawnCell(SpawnCell clone){
        super(clone);
        this.weapons = new ArrayList<>();

        for(Weapon w : clone.getWeapons()){
            this.weapons.add(w);
        }

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