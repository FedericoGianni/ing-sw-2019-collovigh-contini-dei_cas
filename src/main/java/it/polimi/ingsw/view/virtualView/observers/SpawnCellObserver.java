package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.cachemodel.sendables.CachedSpawnCell;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnCellObserver implements Observer {

    private SpawnCell spawnCell;
    private Observers observers;


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.spawnCell = (SpawnCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedSpawnCell(extractWeaponsNames(spawnCell), Model.getMap().cellToCoord(spawnCell));

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }


    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.spawnCell = (SpawnCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedSpawnCell(extractWeaponsNames(spawnCell), Model.getMap().cellToCoord(spawnCell));

        // send the update to the selected player's virtual View

        observers
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }

    private List<String> extractWeaponsNames(SpawnCell spawnCell){

        return spawnCell
                .getWeapons()
                .stream()
                .map(Weapon::getName)
                .collect(Collectors.toList());
    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
