package it.polimi.ingsw.view.virtualView;

import it.polimi.ingsw.view.cacheModel.CachedStats;

public class StatsObserver implements Observer {


    private CachedStats stats;

    public StatsObserver(int playerId) {

    }

    @Override
    public void update(Object object) {

        CachedStats stats = (CachedStats) object;

        this.stats = stats;

    }
}
