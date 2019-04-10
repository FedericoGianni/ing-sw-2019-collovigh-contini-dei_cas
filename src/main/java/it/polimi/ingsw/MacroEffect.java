package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class MacroEffect {

    private List<MicroEffect> microEffects;

    /**
     *
     */
    public MacroEffect() {

        this.microEffects = new ArrayList<>();
    }

    /**
     *
     * @param microEffect is the effect to add to the macro-effect
     */
    public void addMicroEffect(MicroEffect microEffect){

        this.microEffects.add(microEffect.copy());
    }

    /**
     *
     * @param player is the player on who we want to apply this macro-effect
     */
    public void applyOn(Player player){

        //TODO
    }
}
