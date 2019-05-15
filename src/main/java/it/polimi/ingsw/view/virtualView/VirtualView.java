package it.polimi.ingsw.view.virtualView;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.ToView;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public class VirtualView implements ViewInterface {

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

    }

    @Override
    public void spawn(CachedPowerUp powerUp) {

        controller.spawn(powerUp.getType(),powerUp.getColor());

    }

    @Override
    public void sendUpdates(UpdateClass update) {

    }


}
