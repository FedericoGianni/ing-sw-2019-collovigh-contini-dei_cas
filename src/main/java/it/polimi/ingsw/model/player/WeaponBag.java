package it.polimi.ingsw.model.player;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.Subject;
import it.polimi.ingsw.model.player.Bag;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;

import java.util.ArrayList;
import java.util.List;

public class WeaponBag extends Subject implements Bag<Weapon> {


    private List<Weapon> weapons;

    public WeaponBag() {
        this.weapons = new ArrayList<>();
    }

    /**
     *
     * @return a copy of the WeaponList
     */
    @Override
    public List<Weapon> getList() {
        return new ArrayList<>(weapons);
    }

    /**
     *
     * @param item is the Weapon to add
     */
    @Override
    public void addItem(Weapon item) {

        weapons.add(item);

        updateAll(this);

    }

    /**
     *
     * @param item is the weapon that will be deleted from the player inventory
     * @return the ONLY link to that weapon or null if the player do not possess that weapon
     */
    @Override
    public Weapon getItem(Weapon item) throws CardNotPossessedException{

        if (!this.weapons.contains(item)) throw new CardNotPossessedException();
        else {

            // Removes the weapon

            this.weapons.remove(item);

            // updates the observer

            updateAll(this);

            // return the weapon

            return item;

        }
    }

    /**
     *
     * @param item is the weapon it is asked to know if belongs to the player
     * @return true if the card belongs to the player, false otherwise
     */
    @Override
    public Boolean hasItem(Weapon item) {
        return this.weapons.contains(item);
    }
}
