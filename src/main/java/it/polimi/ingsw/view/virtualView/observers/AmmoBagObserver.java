package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoBag;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class AmmoBagObserver implements Observer {

    private CachedAmmoBag ammoBag;
    private final PlayerObserver playerObserver;

    public AmmoBagObserver(PlayerObserver up) {
        this.playerObserver = up;
    }


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.ammoBag = (CachedAmmoBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_BAG,ammoBag, playerObserver.getPlayerId());

        // send the update to the Virtual View

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.ammoBag = (CachedAmmoBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_BAG,ammoBag, playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }
}
