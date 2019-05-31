package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.player.PowerUpBag;
import it.polimi.ingsw.model.powerup.PowerUp;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CachedPowerUpBag extends UpdateClass {

    private final List<CachedPowerUp> powerUpList;

    public CachedPowerUpBag(PowerUpBag upBag, int playerId) {

        super(UpdateType.POWERUP_BAG, playerId);

        powerUpList = new ArrayList<>();

        for (PowerUp up: upBag.getList()){

            powerUpList.add(new CachedPowerUp(up));
        }

    }

    public List<CachedPowerUp> getPowerUpList() {
        return new ArrayList<>(powerUpList);
    }


}
