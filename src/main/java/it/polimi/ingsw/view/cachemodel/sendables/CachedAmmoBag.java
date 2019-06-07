package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.player.AmmoBag;
import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CachedAmmoBag extends UpdateClass {

    private final List<Color> ammoList;

    public CachedAmmoBag(List<Color> ammoList, int playerId) {

        super(UpdateType.STATS,playerId);

        this.ammoList = ammoList;


    }

    public List<Color> getAmmoList() {
        return ammoList;
    }
}
