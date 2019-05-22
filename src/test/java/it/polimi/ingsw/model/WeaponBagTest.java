package it.polimi.ingsw.model;

import it.polimi.ingsw.customsexceptions.CardNotPossessedException;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.weapons.MacroEffect;
import it.polimi.ingsw.model.weapons.NormalWeapon;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.model.player.WeaponBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeaponBagTest {

    @Test
    void getList() {

        List<Weapon> list= new ArrayList<>();
        WeaponBag bag = new WeaponBag();

        list.add(new NormalWeapon("Gun",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>()));
        list.add(new NormalWeapon("Gun1",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>()));
        list.add(new NormalWeapon("Gun2",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>()));

        for (Weapon w:list){

            bag.addItem(w);
        }

        assertEquals(list,bag.getList());


    }

    @Test
    void addItem() {

        WeaponBag bag = new WeaponBag();
        Weapon w = new NormalWeapon("Gun",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>());

        bag.addItem(w);

        assertTrue(bag.getList().contains(w));
    }

    @Test
    void getItem() {

        WeaponBag bag = new WeaponBag();
        Weapon w = new NormalWeapon("Gun",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>());
        Weapon s = null;

        bag.addItem(w);

        assertEquals(1,bag.getList().size());

        try{

            s = bag.getItem(w);

        }catch (CardNotPossessedException e){

            e.printStackTrace();
        }

        assertEquals(0,bag.getList().size());
        assertEquals(w,s);

    }

    @Test
    void hasItem() {

        WeaponBag bag = new WeaponBag();
        Weapon w = new NormalWeapon("Gun",new ArrayList<AmmoCube>(),new ArrayList<MacroEffect>());

        bag.addItem(w);

        assertEquals(1,bag.getList().size());

        assertEquals(true,bag.hasItem(w));


    }
}