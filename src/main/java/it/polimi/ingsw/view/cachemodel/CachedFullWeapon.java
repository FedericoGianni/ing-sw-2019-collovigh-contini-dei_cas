package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class CachedFullWeapon implements Serializable {


    private final String name;
    private final List<Color> firstEffectCost;
    private final List<Color> secondEffectCost;
    private final List<Color> thirdEffectCost;

    public CachedFullWeapon(String name, List<Color> firstEffectCost, List<Color> secondEffectCost, List<Color> thirdEffectCost) {
        this.name = name;
        this.firstEffectCost = firstEffectCost;
        this.secondEffectCost = secondEffectCost;
        this.thirdEffectCost = thirdEffectCost;
    }
}
