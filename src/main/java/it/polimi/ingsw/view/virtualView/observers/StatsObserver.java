package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StatsObserver implements Observer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

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

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public void updateSinge(int playerId, Object object) {

        LOGGER.log(level,"[Stats-Observer] sending Reconnection DATA to player: {0}", playerId);

        // cast the Object in its dynamic type

        this.stats = (CachedStats) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.STATS, stats, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }


}
