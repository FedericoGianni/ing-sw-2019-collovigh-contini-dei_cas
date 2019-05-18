package it.polimi.ingsw.model;

import it.polimi.ingsw.model.powerup.Mover;
import it.polimi.ingsw.model.weapons.Damage;
import it.polimi.ingsw.model.weapons.MacroEffect;
import it.polimi.ingsw.model.weapons.Marker;
import org.junit.jupiter.api.Test;

class MacroEffectTest {

    @Test
    void effectCreator() {
        Damage.populator();
        Marker.populator();
        Mover.populator();//create the MicroEffects Array
        MacroEffect.effectCreator();
        for(int i=0;i<MacroEffect.getMacroEffects().size();i++)//just for seeing something
        {
            System.out.println(MacroEffect.getMacroEffects().get(i).getName());
            for(int j=0;j<MacroEffect.getMacroEffects().get(i).getEffectCost().size();j++)
             System.out.println(MacroEffect.getMacroEffects().get(i).getEffectCost().get(j));
        }

    }
}