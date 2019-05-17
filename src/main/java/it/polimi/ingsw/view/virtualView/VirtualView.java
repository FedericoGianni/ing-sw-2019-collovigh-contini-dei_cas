package it.polimi.ingsw.view.virtualView;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualView implements ViewInterface {

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private int playerId;
    private ToView view;
    private Controller controller;

    public VirtualView(int playerId, Controller controller) {
        this.playerId = playerId;
        this.controller = controller;
        // search in the HashMap with the clients binding the correspondent ToView implementation and stores it here
    }

    @Override
    public void startPhase0() {
        //TODO start the same method inside the real view passing throught network
        LOGGER.info("Virtual View id " + playerId + " received startPhase0 and forwarding it to the real view");
    }

    @Override
    public void spawn(CachedPowerUp powerUp) {
        controller.spawn(powerUp.getType(),powerUp.getColor());
    }

    @Override
    public void sendUpdates(UpdateClass update) {

    }


}
