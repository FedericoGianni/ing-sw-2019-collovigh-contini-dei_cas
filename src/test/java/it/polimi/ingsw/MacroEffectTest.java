package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MacroEffectTest {

    @Test
    void effectCreator() {
        Damage.populator();
        Marker.populator();
        MacroEffect.effectCreator();
        for(int i=0;i<MacroEffect.getMacroEffects().size();i++)
        {
            System.out.println(MacroEffect.getMacroEffects().get(i).getName());
            System.out.println(MacroEffect.getMacroEffects().get(i).getClass());
        }

    }
}