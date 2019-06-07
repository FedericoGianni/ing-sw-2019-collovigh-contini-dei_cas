package it.polimi.ingsw.view.cachemodel;

import it.polimi.ingsw.model.Color;

import java.io.Serializable;
import java.util.List;

public class CachedFullWeapon implements Serializable {


    private final String name;
    private final List<Color> buyCost;
    private final List<Color> firstEffectCost;
    private final List<Color> secondEffectCost;
    private final List<Color> thirdEffectCost;

    public CachedFullWeapon(String name, List<Color> buyCost, List<Color> firstEffectCost, List<Color> secondEffectCost, List<Color> thirdEffectCost) {
        this.name = name;
        this.buyCost = buyCost;
        this.firstEffectCost = firstEffectCost;
        this.secondEffectCost = secondEffectCost;
        this.thirdEffectCost = thirdEffectCost;
    }
}
