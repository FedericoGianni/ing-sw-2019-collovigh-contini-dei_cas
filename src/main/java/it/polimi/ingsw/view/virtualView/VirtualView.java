package it.polimi.ingsw.view.virtualView;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualView implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.INFO;

    private int playerId;
    private ToView view;
    private Controller controller;

    private boolean isConnected = true;

    public VirtualView(int playerId, Controller controller, ToView toView) {
        this.playerId = playerId;
        this.controller = controller;
        // search in the HashMap with the clients binding the correspondent ToView implementation and stores it here
        this.view = toView;
    }

    public void setView(ToView view) {
        this.view = view;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }


    //methods to forward to the corresponding view throught network


    @Override
    public void startSpawn() {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId); // can change if player disconnect -> to be refreshed every method

        LOGGER.info("Virtual View id " + playerId + " received startPhase0 and forwarding it to the real view");

        // calls the function on the ToClient interface

        view.startSpawn();

    }

    @Override
    public void startPowerUp(){

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);
        LOGGER.info("Virtual View id " + playerId + " received startPowerUp and forwarding it to the real view");
        view.startPowerUp();
    }

    @Override
    public void startAction() {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);

        //TODO start the same method inside the real view passing thorught network
    }

    @Override
    public void startReload() {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);

        //TODO start the same method inside the real view passing throught network
        LOGGER.info("Virtual View id " + playerId + " received startReload and forwarding it to the real view");

    }


    @Override
    public void startGame() {
        view.startGame();
    }

    @Override
    public void sendUpdates(UpdateClass update) {

        // refresh the ToClient reference

        this.view = Server.getClient(playerId);

        System.out.println("[VIRTUAL-VIEW] view = " + view);

        System.out.println("[VIRTUAL-VIEW] update = " + update );

        // sends the updates to the net if the client is connected

        if (view != null)  this.view.sendUpdate(update);

        LOGGER.log(level, " send update to client: " + playerId + " update: "+ update.getType());

    }



    //methods called by the view to the virtual view to call controller


    @Override
    public void spawn(CachedPowerUp powerUp) {
        controller.spawn(powerUp.getType(),powerUp.getColor());
    }

    @Override
    public void doAction(JsonAction jsonAction) {

        controller.doAction(jsonAction);
    }

    @Override
    public void useMarker(Color color, int playerId) {

    }

}
