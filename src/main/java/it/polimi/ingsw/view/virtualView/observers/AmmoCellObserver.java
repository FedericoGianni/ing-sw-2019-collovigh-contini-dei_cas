package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedGame;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;
import it.polimi.ingsw.view.cachemodel.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class AmmoCellObserver implements Observer {

    private CachedAmmoCell ammoCell;
    private Observers observers;


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.ammoCell = new CachedAmmoCell((AmmoCell) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.CELL_AMMO, ammoCell);

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.ammoCell = (CachedAmmoCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.CELL_AMMO, ammoCell, -1);

        // send the update to the selected player's virtual View

        observers
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
