package it.polimi.ingsw.view.virtualView;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.network.serveronly.Server;
import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualView implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private int playerId;
    private ToView view;
    private Controller controller;

    public VirtualView(int playerId, Controller controller, ToView toView) {
        this.playerId = playerId;
        this.controller = controller;
        // search in the HashMap with the clients binding the correspondent ToView implementation and stores it here
        this.view = toView;
    }

    public void setView(ToView view) {
        this.view = view;
    }

    public int getPlayerId() { return playerId; }

    //methods to forward to the corresponding view throught network

    /**
     * {@inheritDoc}
     */
    @Override
    public void startSpawn() {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId); // can change if player disconnect -> to be refreshed every method

        LOGGER.log(level,"[Virtual View] id {0} received startPhase0 and forwarding it to the real view",playerId);

        // calls the function on the ToClient interface
        if(view != null)
            view.startSpawn();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startPowerUp(){

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);
        LOGGER.log(level,"[Virtual View] id {0} received startPowerUp and forwarding it to the real view", playerId);

        if(view != null)
            view.startPowerUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter) {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);
        if(view != null)
            view.startAction(isFrenzy, isBeforeFrenzyStarter);

        LOGGER.log(level,"[Virtual View] id {0} received startAction and forwarding it to the real view",playerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReload() {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);
        if(view != null)
            view.startReload();

        LOGGER.log(level,"[Virtual View] id {0} received startReload and forwarding it to the real view",playerId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askGrenade() {
        this.view = Server.getClient(playerId);
        LOGGER.log(level,"[Virtual View] id {0} forwarding askGrenade to view", playerId);
        if(view != null)
            view.askGrenade();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame() {
        this.view = Server.getClient(playerId);
        if(view != null)
            view.startGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(String s) {
        this.view = Server.getClient(playerId);
        if(view != null)
            view.show(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUpdates(UpdateClass update) {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);

        // sends the updates to the net if the client is connected

        if (view != null)  this.view.sendUpdate(update);

        LOGGER.log(level, () -> " send update to client: " + playerId + " update: "+ update.getType() + "\n update content: " + update);

    }



    //methods called by the view to the virtual view to call controller

    /**
     * {@inheritDoc}
     */
    @Override
    public void spawn(CachedPowerUp powerUp) {
        controller.spawn(powerUp.getType(),powerUp.getColor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doAction(JsonAction jsonAction) {

        controller.doAction(jsonAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endGame() {

        if(view != null)
            view.endGame();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean askMoveValid(int row, int column, Directions direction) {

        return controller.askMoveValid(row, column, direction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reDoFrenzyAtomicShoot() {
        if(view != null)
            view.reDoFrenzyAtomicShoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValidMove(boolean b) {
        //i don't need this method here
    }


}
