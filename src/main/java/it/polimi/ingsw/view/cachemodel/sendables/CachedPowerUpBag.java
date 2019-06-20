package it.polimi.ingsw.view.cachemodel.sendables;


import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.ArrayList;
import java.util.List;

public class CachedPowerUpBag extends UpdateClass {

    private final List<CachedPowerUp> powerUpList ;

    public CachedPowerUpBag(List<CachedPowerUp> powerUpList, int playerId) {

        super(UpdateType.POWERUP_BAG, playerId);

        this.powerUpList = powerUpList;

    }

    public List<CachedPowerUp> getPowerUpList() {
        return new ArrayList<>(powerUpList);
    }

    public List<Color> getPowerUpColorList() {

        List<Color> powerUpColorList = new ArrayList<>();

        for(CachedPowerUp p : powerUpList){
            powerUpColorList.add(p.getColor());
        }

        return powerUpColorList;
    }


}
