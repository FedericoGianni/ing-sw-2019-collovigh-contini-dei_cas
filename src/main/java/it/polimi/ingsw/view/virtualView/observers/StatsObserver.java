package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.player.Stats;
import it.polimi.ingsw.view.cachemodel.sendables.CachedStats;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StatsObserver implements Observer {

    private static final int MAX_DMG = 12;

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private final PlayerObserver playerObserver;
    private Stats stats;

    public StatsObserver(PlayerObserver up) {

        this.playerObserver = up;
    }

    @Override
    public synchronized void update(Object object) {


        // cast the Object in its dynamic type

        this.stats = (Stats) object;

        // check if the player is dead

        checkPlayerDead((Stats) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedStats(playerObserver.getPlayerId(),stats.getScore(),stats.getDeaths(),stats.getOnline(),stats.getMarks(),stats.getDmgTaken(), Model.getMap().cellToCoord(stats.getCurrentPosition()));

        // send the update to the Virtual View

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public synchronized void updateSinge(int playerId, Object object) {

        LOGGER.log(level,"[Stats-Observer] sending Reconnection DATA to player: {0}", playerId);

        this.stats = (Stats) object;

        // check if the player is dead

        checkPlayerDead((Stats) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedStats(playerObserver.getPlayerId(),stats.getScore(),stats.getDeaths(),stats.getOnline(),stats.getMarks(),stats.getDmgTaken(), Model.getMap().cellToCoord(stats.getCurrentPosition()));

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }

    /**
     * This method will check if the player is dead and if that is the case will notify the controller
     * @param stats is the stats updated
     */
    private void checkPlayerDead(Stats stats){

        // if the player died

        if (stats.getDmgTaken().size() > MAX_DMG -1){

            if (stats.getDmgTaken().size() >= MAX_DMG){

                // if the player gets overkilled

                playerObserver.getTopClass().getController().overKillPlayer(stats.getPlayerId());

            }else{

                // if the player gets killed

                playerObserver.getTopClass().getController().killPlayer(stats.getPlayerId());
            }

        }
    }
}
