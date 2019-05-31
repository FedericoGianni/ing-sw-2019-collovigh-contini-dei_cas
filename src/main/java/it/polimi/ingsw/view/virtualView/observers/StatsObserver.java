package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StatsObserver implements Observer {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private final PlayerObserver playerObserver;
    private Stats stats;

    public StatsObserver(PlayerObserver up) {

        this.playerObserver = up;
    }

    @Override
    public void update(Object object) {


        // cast the Object in its dynamic type

        this.stats = (Stats) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedStats(stats, playerObserver.getPlayerId());

        // send the update to the Virtual View

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public void updateSinge(int playerId, Object object) {

        LOGGER.log(level,"[Stats-Observer] sending Reconnection DATA to player: {0}", playerId);

        this.stats = (Stats) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedStats(stats, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }


}
