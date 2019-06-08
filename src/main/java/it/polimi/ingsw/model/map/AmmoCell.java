package it.polimi.ingsw.model.map;

import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.virtualView.observers.Observers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class AmmoCell extends Cell {

    private AmmoCard ammoPlaced;

    /**
     * Default constructor
     */
    public AmmoCell() {

        super();

        setAmmoCell(true);

        // Place the AmmoCard

        ammoPlaced = AmmoCard.generateRandCard();

        // Sets the Observer

        if (Observers.isInitialized()) this.addObserver(Observers.getAmmoCellObserver());

    }

    /**
     * This constructor is used to return a copy of the AmmoCell passed as a parameter
     * @param clone AmmoCell to be cloned
     */
    public AmmoCell(AmmoCell clone){

        super(clone);
        this.ammoPlaced = clone.ammoPlaced;
    }


    /**
     * @return a randomly generated Ammo Card, based on the probability of the real on-board game deck
     * the ammoPlaced is generated randomly inside the creator so that the ammo is the same for all players and is not
     * generated randomly every time a Player wants to check what Ammo is placed inside this Cell
     */
    @Override
    public AmmoCard getAmmoPlaced() {

        return ammoPlaced;
    }

    /**
     * @return the Ammo picked up by Player inside this Cell, also it generates a new PowerUp to be placed inside Cell
     */
    @Override
    public AmmoCard pickAmmoPlaced() {

        AmmoCard tempAmmo = ammoPlaced;

        ammoPlaced = null;

        updateAll(this);

        return tempAmmo;
    }

    @Override
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {
        return null;
    }

    public void generateAmmoCard(){

        if (ammoPlaced == null) ammoPlaced = AmmoCard.generateRandCard();

        updateAll(this);

    }


}
