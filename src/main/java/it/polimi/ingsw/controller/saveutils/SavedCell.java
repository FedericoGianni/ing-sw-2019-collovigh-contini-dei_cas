package it.polimi.ingsw.controller.saveutils;

import it.polimi.ingsw.controller.Parser;
import it.polimi.ingsw.customsexceptions.NotEnoughAmmoException;
import it.polimi.ingsw.model.ammo.AmmoCard;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.model.map.Cell;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.weapons.Weapon;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to serialize a Cell in a file to be read, allowing game persistence
 */
public class SavedCell extends Cell implements Serializable {

    /**
     * ammo card inside the cell (if the cell contains ammo)
     */
    private final AmmoCard ammoCard;

    /**
     * weapon list inside the cell in case it contains weapons
     */
    private final List<String> weaponList;

    /**
     * Constructor wich takes as paramter a SpawnCell
     * @param cell cell of Spawn type from Model
     */
    public SavedCell(SpawnCell cell) {

        this.copyParamFrom(cell);

        this.ammoCard = null;


        this.weaponList = cell.getWeapons()
                .stream()
                .map( weapon -> weapon.getName())
                .collect(Collectors.toList());


    }

    /**
     * Constructor wich takes as paramter an ammo cell
     * @param cell cell of ammo type from Model
     */
    public SavedCell(AmmoCell cell) {

        this.copyParamFrom(cell);

        this.ammoCard = cell.getAmmoPlaced();

        this.weaponList = null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAmmoCell() {
        return (ammoCard != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmmoCard getAmmoPlaced() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmmoCard pickAmmoPlaced() {

        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Weapon> getWeapons() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedCell getSaveVersionOfCell() {

        throw new UnsupportedOperationException();

    }

    /**
     *
     * @return a Cell generated from this savedCell class istance
     */
    public Cell getRealCell(){

        Cell finalCell;

        if (this.weaponList == null){

            AmmoCell cell = new AmmoCell();

            cell.setAmmoPlaced((this.ammoCard == null) ? AmmoCard.generateRandCard() : this.ammoCard);

            finalCell = cell;

        } else {

            SpawnCell cell = new SpawnCell();


            List<Weapon> weapons = this.weaponList
                    .stream()
                    .map( name -> Parser.getWeaponByName(name))
                    .collect(Collectors.toList());



            cell.setWeapons(weapons);

            finalCell = cell;
        }


        finalCell.copyParamFrom(this);

        return finalCell;
    }
}
