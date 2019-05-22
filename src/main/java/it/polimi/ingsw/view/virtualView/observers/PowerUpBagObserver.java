package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedPowerUpBag;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;

public class PowerUpBagObserver implements Observer {

    private CachedPowerUpBag powerUpBag = null;
    private final PlayerObserver playerObserver;

    public PowerUpBagObserver(PlayerObserver up) {

        this.playerObserver = up;

    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.powerUpBag =(CachedPowerUpBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.POWERUP_BAG, powerUpBag, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerObserver.getPlayerId())
                .sendUpdates(updateClass);
    }


}
