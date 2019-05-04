package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.player.AmmoBag;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SpawnCell extends Cell {


    /**
     * attribute useful for canSee method, which need to check if the cell has already been visited or not
     */
    private boolean visit;
    private List<Weapon> weapons;

    /**
     * Default constructor
     */
    public SpawnCell() {
        this.visit=false;
        this.weapons = new ArrayList<>();
    }


    /**
     * This constructor is used to return a copy of the SpawnCell passed as a parameter
     * @param clone SpawnCell to be cloned
     */
    public SpawnCell(SpawnCell clone){
        super(clone);
        this.weapons = new ArrayList<>(clone.weapons);

    }


    /**
     * this method populate the weapon list in the spawnPoint
     */
    public void populateWeapon(){

        for (int i = 0; i < (3-weapons.size()); i++) {

            this.weapons.add(Model.getGame().drawWeapon());
        }

    }


    /**
     * @return a list of Weapons who are available for sell inside this SpawnCell
     */
    public List<Weapon> getWeapons() {
        if (this.weapons.isEmpty()) this.populateWeapon();
        return weapons;
    }


    /**
     * @param w is the weapon that will be bought
     * @param cash is the ammoBag of the player who wants to buy it
     * @return true if the operation succeeded
     */
    public Boolean buy(Weapon w, AmmoBag cash) {

        //TODO
        return false;
    }

}