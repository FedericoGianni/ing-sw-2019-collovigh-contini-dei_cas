package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;

public class WeaponBagObserver implements Observer {

    private CachedWeaponBag cachedWeaponBag;
    private final PlayerObserver playerObserver;

    public WeaponBagObserver(PlayerObserver playerObserver) {
        this.playerObserver = playerObserver;
    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.cachedWeaponBag = (CachedWeaponBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_BAG,cachedWeaponBag, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerObserver.getPlayerId())
                .sendUpdates(updateClass);
    }
}
