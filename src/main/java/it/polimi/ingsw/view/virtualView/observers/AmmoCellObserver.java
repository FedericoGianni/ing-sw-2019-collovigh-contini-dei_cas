package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.List;
import java.util.stream.Collectors;

public class AmmoCellObserver implements Observer {

    private AmmoCell ammoCell;
    private Observers observers;


    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.ammoCell = (AmmoCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoCell(extractAmmoList(ammoCell),ammoCell.getAmmoPlaced().getPowerUp(), Model.getMap().cellToCoord(ammoCell));

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.ammoCell = (AmmoCell) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoCell(extractAmmoList(ammoCell),ammoCell.getAmmoPlaced().getPowerUp(), Model.getMap().cellToCoord(ammoCell));

        // send the update to the selected player's virtual View

        observers
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }

    private List<Color> extractAmmoList(AmmoCell ammoCell){

        return ammoCell
                .getAmmoPlaced()
                .getAmmoList()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());
    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
