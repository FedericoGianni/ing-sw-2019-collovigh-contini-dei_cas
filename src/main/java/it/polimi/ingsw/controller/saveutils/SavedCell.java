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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SavedCell extends Cell implements Serializable {

    private final AmmoCard ammoCard;

    private final List<String> weaponList;


    public SavedCell(SpawnCell cell) {

        this.copyParamFrom(cell);

        this.ammoCard = null;


        this.weaponList = cell.getWeapons()
                .stream()
                .map( weapon -> weapon.getName())
                .collect(Collectors.toList());


    }

    public SavedCell(AmmoCell cell) {

        this.copyParamFrom(cell);

        this.ammoCard = cell.getAmmoPlaced();

        this.weaponList = null;

    }

    @Override
    public Boolean isAmmoCell() {
        return (ammoCard != null);
    }

    @Override
    public AmmoCard getAmmoPlaced() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AmmoCard pickAmmoPlaced() {

        throw new UnsupportedOperationException();
    }

    @Override
    public List<Weapon> getWeapons() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Weapon buy(Weapon w, Player player) throws NotEnoughAmmoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SavedCell getSaveVersionOfCell() {

        throw new UnsupportedOperationException();

    }

    public Cell getRealCell(){

        Cell finalCell;

        if (this.weaponList == null){

            AmmoCell cell = new AmmoCell();

            cell.setAmmoPlaced(this.ammoCard);

            if (this.ammoCard == null) cell.generateAmmoCard();

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
