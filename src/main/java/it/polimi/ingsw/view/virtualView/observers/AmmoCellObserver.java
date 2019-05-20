package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class AmmoCellObserver implements Observer {

    private CachedAmmoCell ammoCell;
    private Observers observers;


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.ammoCell = new CachedAmmoCell((AmmoCell) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass(UpdateType.AMMO_CELL, ammoCell);

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
