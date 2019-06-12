package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.utils.Color;

import java.io.Serializable;
import java.util.List;

public class CachedFullWeapon implements Serializable {


    private final String name;
    private final List<Color> firstEffectCost;
    private final List<Color> secondEffectCost;
    private final List<Color> thirdEffectCost;
    private final List<EffectType> effectTypes;
    private final List<EffectRequirements> effectRequirements;
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
}
