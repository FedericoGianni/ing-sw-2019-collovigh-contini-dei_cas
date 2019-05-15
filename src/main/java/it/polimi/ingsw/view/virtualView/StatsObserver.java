package it.polimi.ingsw.view.virtualView;

import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;

public class StatsObserver implements Observer {


    private CachedStats stats;

    public StatsObserver() {

    }

    @Override
    public void update(Object object) {

        this.stats = (CachedStats) object;

        //send updates to client

    }


}
