package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;

public class StatsObserver implements Observer {

    private final PlayerObserver playerObserver;
    private CachedStats stats;

    public StatsObserver(PlayerObserver up) {

        this.playerObserver = up;
    }

    @Override
    public void update(Object object) {


        // cast the Object in its dynamic type

        this.stats = (CachedStats) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.STATS, stats, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerObserver.getPlayerId())
                .sendUpdates(updateClass);

    }


}
