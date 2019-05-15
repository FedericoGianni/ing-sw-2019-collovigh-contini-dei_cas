package it.polimi.ingsw.view.virtualView;

import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;

public class AmmoBagObserver implements Observer {

    private CachedAmmoBag ammoBag;

    @Override
    public void update(Object object) {

        this.ammoBag = (CachedAmmoBag) object;
    }
}
