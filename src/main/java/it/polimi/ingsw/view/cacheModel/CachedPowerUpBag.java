package it.polimi.ingsw.view.cacheModel;

import it.polimi.ingsw.model.player.PowerUpBag;
import it.polimi.ingsw.model.powerup.PowerUp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CachedPowerUpBag implements Serializable {

    private final List<CachedPowerUp> powerUpList;

    public CachedPowerUpBag(PowerUpBag upBag) {

        powerUpList = new ArrayList<>();

        for (PowerUp up: upBag.getList()){

            powerUpList.add(new CachedPowerUp(up));
        }

    }

    public List<CachedPowerUp> getPowerUpList() {
        return new ArrayList<>(powerUpList);
    }
}
