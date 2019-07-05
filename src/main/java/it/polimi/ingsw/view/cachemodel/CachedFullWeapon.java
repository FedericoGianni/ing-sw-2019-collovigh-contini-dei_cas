package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.utils.Color;

import java.io.Serializable;
import java.util.List;

/**
 * Class to store all the information about a weapon needed by a user to forward a shoot
 * It contains every effect cost, reload cost, effect descriptions, and effect requirements so that the interface
 * knows what to ask the player to shoot with a specific weapon
 */
public class CachedFullWeapon implements Serializable {

    /**
     * Name of the weapon
     */
    private final String name;

    /**
     * Reload cost, since the base effect cost is always free
     */
    private final List<Color> firstEffectCost;

    /**
     * Cost of the second effect (if the weapon has only base effect this is null)
     */
    private final List<Color> secondEffectCost;

    /**
     * Cost of the third effect if present (if the weapon has only 2 effect this is null)
     */
    private final List<Color> thirdEffectCost;

    /**
     * List of EffectType for each weapon effect containing useful information about effects type like if you can choose
     * alternatevely between base or first effect or you can choose both
     */
    private final List<EffectType> effectTypes;

    /**
     * List of effect requirements such as the number of targets and if it needs a cell, for every effect
     */
    private final List<EffectRequirements> effectRequirements;

    /**
     * Description of the effects read from a json file
     */
    private final List<String> effectsDescriptions;

    public CachedFullWeapon(String name, List<Color> firstEffectCost, List<Color> secondEffectCost, List<Color> thirdEffectCost, List<EffectType> effectTypes, List<EffectRequirements> effectRequirements, List<String> effectsDescriptions) {
        this.name = name;
        this.firstEffectCost = firstEffectCost;
        this.secondEffectCost = secondEffectCost;
        this.thirdEffectCost = thirdEffectCost;

        this.effectTypes = effectTypes;
        this.effectRequirements = effectRequirements;
        this.effectsDescriptions = effectsDescriptions;
    }

    public String getName() {
        return name;
    }

    public List<Color> getFirstEffectCost() {
        return firstEffectCost;
    }

    public List<Color> getSecondEffectCost() {
        return secondEffectCost;
    }

    public List<Color> getThirdEffectCost() {
        return thirdEffectCost;
    }

    public List<EffectType> getEffectTypes() { return effectTypes; }

    public List<EffectRequirements> getEffectRequirements() { return effectRequirements; }

    public List<String> getEffectsDescriptions() { return effectsDescriptions; }

    /**
     * Buy effects needs to ignore the first cube
     * @return the cost to buy a weapon, since it is different from the cost to reload it
     */
    public List<Color> getBuyEffect(){

        return firstEffectCost.subList(1,firstEffectCost.size());
    }
}
