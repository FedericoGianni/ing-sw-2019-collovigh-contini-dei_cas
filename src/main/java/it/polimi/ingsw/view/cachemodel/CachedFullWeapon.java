package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class CachedFullWeapon implements Serializable {


    private final String name;
    private final List<Color> firstEffectCost;
    private final List<Color> secondEffectCost;
    private final List<Color> thirdEffectCost;
    private final EffectType secondEffectType;
    private final EffectType thirdEffectType;

    public CachedFullWeapon(String name, List<Color> firstEffectCost, List<Color> secondEffectCost, List<Color> thirdEffectCost, EffectType secondEffectType, EffectType thirdEffectType) {
        this.name = name;
        this.firstEffectCost = firstEffectCost;
        this.secondEffectCost = secondEffectCost;
        this.thirdEffectCost = thirdEffectCost;

        this.secondEffectType = secondEffectType;
        this.thirdEffectType = thirdEffectType;
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
}