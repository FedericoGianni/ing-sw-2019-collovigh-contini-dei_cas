package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.map.SpawnCell;
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

        this.spawnCell = new CachedSpawnCell((SpawnCell) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = spawnCell;

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }


    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.spawnCell = new CachedSpawnCell((SpawnCell) object);

        // encapsulate the update in the update Class

        UpdateClass updateClass = spawnCell;

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
