package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.player.PowerUpBag;
import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

public class PowerUpBagObserver implements Observer {

    private PowerUpBag powerUpBag = null;
    private final PlayerObserver playerObserver;

    public PowerUpBagObserver(PlayerObserver up) {

        this.playerObserver = up;

    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.powerUpBag =(PowerUpBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedPowerUpBag(powerUpBag,playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerObserver.getPlayerId())
                .sendUpdates(updateClass);
    }


    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.powerUpBag =(PowerUpBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedPowerUpBag(powerUpBag,playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }


}
