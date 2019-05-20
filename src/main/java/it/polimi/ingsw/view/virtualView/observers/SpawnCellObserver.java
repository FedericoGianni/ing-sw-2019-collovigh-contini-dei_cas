package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.virtualView.VirtualView;

public class SpawnCellObserver implements Observer {

    private CachedSpawnCell spawnCell;
    private Observers observers;

    public SpawnCellObserver(){

    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.spawnCell = (CachedSpawnCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new UpdateClass( UpdateType.SPAWN_CELL, spawnCell);

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }


    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
