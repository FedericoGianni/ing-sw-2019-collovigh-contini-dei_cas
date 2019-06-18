package it.polimi.ingsw.model.map;

import it.polimi.ingsw.controller.saveutils.SavedCell;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class SpawnCell extends Cell {



    private List<Weapon> weapons = new ArrayList<>();

    /**
     * Default constructor
     */
    public SpawnCell() {

        // add observer to the class
        if (Observers.isInitialized()) addObserver(Observers.getSpawnCellObserver());
    }

    @Override
    public Boolean isAmmoCell() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmmoCard getAmmoPlaced() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmmoCard pickAmmoPlaced() {
        return null;
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
     * {@inheritDoc}
     */
    @Override
    public List<Weapon> getWeapons() {
        if (this.weapons.isEmpty()) this.populateWeapon();
        return new ArrayList<>(weapons);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {

        if (player.canPay(w.getCost())){

            weapons.remove(w);

            this.populateWeapon();

            updateAll(this);

            return w;


        }else throw new NotEnoughAmmoException();

    }

    /**
     * This method is meant for save only
     * @param weapons is a list of weapon to add
     */
    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    @Override
    public SavedCell getSaveVersionOfCell() {

        return new SavedCell(this);

    }

}