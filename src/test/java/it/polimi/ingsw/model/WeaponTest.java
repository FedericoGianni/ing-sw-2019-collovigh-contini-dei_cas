package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void weaponsCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        Weapon.weaponsCreator();
        for(int i=0;i<Weapon.getWeapons().size();i++)
        {
            System.out.println(Weapon.getWeapons().get(i).getName());
            for(int j=0;j<Weapon.getWeapons().get(i).getEffects().size();j++)
            {
                System.out.println(Weapon.getWeapons().get(i).getEffects().get(j).getName());
            }

        }
    }
}