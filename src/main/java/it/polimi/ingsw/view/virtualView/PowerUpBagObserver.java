package it.polimi.ingsw.view.virtualView;

import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;

public class PowerUpBagObserver implements Observer{

    private CachedPowerUpBag powerUpBag = null;

    public PowerUpBagObserver() {

    }

    @Override
    public void update(Object object) {

        this.powerUpBag =(CachedPowerUpBag) object;

        // send updates to client
    }


}
