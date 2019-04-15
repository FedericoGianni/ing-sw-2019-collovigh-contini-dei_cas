package it.polimi.ingsw.model;

import java.util.*;

/**
 * 
 */
public class Weapon {

    private String name;
    private boolean isLoaded;
    private List<AmmoCube> cost;
    private List<MacroEffect> effects;

    /**
     * Constructor,
     *
     * isLoaded is set on true because Weapons are loaded when bought
     * effects are not filled in the creator
     */
    public Weapon(String name, List<AmmoCube> cost) {

        this.name = name;
        this.isLoaded = true;
        this.cost = cost;
        effects = new ArrayList<>();
    }

    public Weapon(Weapon clone){
        this.name = clone.name;
        this.isLoaded = clone.isLoaded;
        this.cost = clone.cost;
        this.effects = new ArrayList<>();

        for(MacroEffect e : clone.effects){
            this.effects.add(e);
        }
    }


    /**
     * @return Boolean for if the weapon is loaded
     */
    public Boolean isLoaded() {

        return this.isLoaded;
    }

    /**
     * @return true only if the player has enough ammo for reloading the Weapon
     */
    public Boolean canBeReloaded() {

        //TODO
        return false;
    }

    /**
     *  reload the weapon
     */
    public void reload() {
        // TODO implement here
    }

    /**
     * @return the cost of buying this Weapon so the cost of recharge without the first cube
     */
    public List<AmmoCube> getCost() {

        return cost.subList(1,cost.size());
    }

    /**
     *
     * @return the name of the Weapon
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the list of macro-effects
     */
    public List<MacroEffect> getEffects() {
        return effects;
    }

    /**
     *
     * @param macroEffect is the effect that will be added to the Weapon
     */
    public void addMacroEffect(MacroEffect macroEffect){

        this.effects.add(macroEffect);
    }
}