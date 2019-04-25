package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

class NormalWeaponTest {

    @Test
    void weaponsCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();
        MacroEffect.effectCreator();
        NormalWeapon.weaponsCreator();
        for(int i = 0; i< NormalWeapon.getNormalWeapons().size(); i++)
        {
            System.out.println(NormalWeapon.getNormalWeapons().get(i).getName());
            for(int j = 0; j< NormalWeapon.getNormalWeapons().get(i).getEffects().size(); j++)
            {
                System.out.println(NormalWeapon.getNormalWeapons().get(i).getEffects().get(j).getName());
            }

        }
    }
}