package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.sendables.CachedAmmoCell;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AmmoCellObserver implements Observer {


    private Observers observers;


    @Override
    public void update(Object object) {

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoCell(extractAmmoList((AmmoCell) object),extractPowerUp((AmmoCell) object), Model.getMap().cellToCoord((AmmoCell) object));

        // send the update to the Virtual View

        for (VirtualView virtualView : observers.getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }

    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedAmmoCell(extractAmmoList((AmmoCell) object),extractPowerUp((AmmoCell) object), Model.getMap().cellToCoord((AmmoCell) object));

        // send the update to the selected player's virtual View

        observers
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);

    }

    private List<Color> extractAmmoList(AmmoCell ammoCell){

        if (ammoCell.getAmmoPlaced() != null) {
            return ammoCell
                    .getAmmoPlaced()
                    .getAmmoList()
                    .stream()
                    .map(AmmoCube::getColor)
                    .collect(Collectors.toList());
        }else {

            return  new ArrayList<>();
        }
    }

    private Boolean extractPowerUp(AmmoCell ammoCell){

        return ammoCell.getAmmoPlaced() != null && ammoCell.getAmmoPlaced().getPowerUp() ;
    }

    public void setObservers(Observers observers) {
        this.observers = observers;
    }
}
