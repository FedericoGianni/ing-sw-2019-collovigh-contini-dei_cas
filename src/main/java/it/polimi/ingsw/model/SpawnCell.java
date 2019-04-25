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
        this.normalWeapons = new ArrayList<>();

        for(NormalWeapon w : clone.getNormalWeapons()){
            this.normalWeapons.add(w);
        }

    }

    /**
     * attribute useful for canSee method, which need to check if the cell has already been visited or not
     */
    private boolean visit;

    /**
     * 
     */
    private List<NormalWeapon> normalWeapons;

    /**
     * @return a list of Weapons who are avaiable for sell inside this SpawnCell
     */
    public List<NormalWeapon> getNormalWeapons() {
        return normalWeapons;
    }


    /**
     * @param w 
     * @return
     */
    public Boolean buy(NormalWeapon w) {
        // TODO implement here
        return null;
    }

}