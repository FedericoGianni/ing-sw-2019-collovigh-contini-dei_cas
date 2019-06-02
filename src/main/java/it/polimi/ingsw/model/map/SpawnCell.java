package it.polimi.ingsw.model.map;

import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // add observer to the class
        if (Observers.isInitialized()) addObserver(Observers.getSpawnCellObserver());
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

        int prevSize = weapons.size();

        for (int i = 0; i < ( 3 - prevSize ); i++) {

            this.weapons.add(Model.getGame().drawWeapon());
        }

        updateAll(this);

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
     * @param player is the  the player who wants to buy it
     * @return true if the operation succeeded
     */
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {

        if (player.canPay(w.getCost())){

            weapons.remove(w);
            this.populateWeapon();

            updateAll(this);

            return w;


        }else throw new NotEnoughAmmoException();

    }

}