package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.controller.TurnPhase;
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
    private static Level level = Level.INFO;

    private final PlayerObserver playerObserver;
    private Stats previousStats;
    private Stats stats;

    public StatsObserver(PlayerObserver up) {

        this.playerObserver = up;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public synchronized void update(Object object) {

        // get the update Class

        UpdateClass updateClass = genUpdateClass(object);

        // send the update to the Virtual View

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateSinge(int playerId, Object object) {

        LOGGER.log(level,"[Stats-Observer] sending Reconnection DATA to player: {0}", playerId);

        // get the update Class

        UpdateClass updateClass = genUpdateClass(object);

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

        if ( (previousStats != null ) && ( previousStats.getDmgTaken().size() < MAX_DMG -1 ) && (stats.getDmgTaken().size() >= MAX_DMG -1) ) {

            if (stats.getDmgTaken().size() >= MAX_DMG){

                // if the player gets overkilled

                playerObserver.getTopClass().getController().overKillPlayer(stats.getPlayerId());

            }else{

                // if the player gets killed

                playerObserver.getTopClass().getController().killPlayer(stats.getPlayerId());
            }

        }
    }

    /**
     * This method perform a check on whether or not the player has been shot and if that is the case notify the controller
     * @param stats is the stats class received
     */
    private void notifyPlayerShotToController(Stats stats){

        if (previousStats != null) {

            // if the player has received dmg or marks in the action phase and is not already in the shotList

            if ((playerObserver.getTopClass().getController().getTurnPhase().equals(TurnPhase.ACTION1) || playerObserver.getTopClass().getController().getTurnPhase().equals(TurnPhase.ACTION2)) && ((!previousStats.getDmgTaken().equals(stats.getDmgTaken())) || (!previousStats.getMarks().equals(stats.getMarks()))) && (!playerObserver.getTopClass().getController().getShotPlayerThisTurn().contains(stats.getPlayerId())) ) {

                LOGGER.log(level, "[Stats-Observer] player added to shot list: {0} ", stats.getPlayerId());

                // adds the player to the list of shot player if not present

                playerObserver.getTopClass().getController().getShotPlayerThisTurn().add(stats.getPlayerId());

            }
        }
    }

    /**
     * This method generates the updateClass to send to the Views
     * @param object is the observable class that has been modified
     * @return a new UpdateClass containing the updates to send to the view
     */
    private UpdateClass genUpdateClass(Object object){

        // stores the previous value

        this.previousStats = stats;

        // cast the Object in its dynamic type  and stores a clone of it

        this.stats = new Stats((Stats) object);

        // check if the player has been shot

        notifyPlayerShotToController((Stats) object);

        // check if the player is dead

        checkPlayerDead((Stats) object);

        // encapsulate the update in the update Class

        return new CachedStats(playerObserver.getPlayerId(),stats.getScore(),stats.getDeaths(),stats.getOnline(),stats.getMarks(),stats.getDmgTaken(), Model.getMap().cellToCoord(stats.getCurrentPosition()));
    }
}
