package it.polimi.ingsw.view.virtualView.observers;

import it.polimi.ingsw.model.player.WeaponBag;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.cachemodel.sendables.CachedWeaponBag;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.virtualView.VirtualView;

import java.util.List;
import java.util.stream.Collectors;

public class WeaponBagObserver implements Observer {

    private WeaponBag weaponBag;
    private final PlayerObserver playerObserver;

    public WeaponBagObserver(PlayerObserver playerObserver) {
        this.playerObserver = playerObserver;
    }

    @Override
    public void update(Object object) {

        // cast the Object in its dynamic type

        this.weaponBag = (WeaponBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedWeaponBag(extractWeaponsName(weaponBag), extractLoaded(weaponBag), playerObserver.getPlayerId());

        // send the update to all the Virtual Views

        for (VirtualView virtualView : playerObserver.getTopClass().getController().getVirtualViews()){

            virtualView.sendUpdates(updateClass);
        }
    }

    @Override
    public void updateSinge(int playerId, Object object) {

        // cast the Object in its dynamic type

        this.weaponBag = (WeaponBag) object;

        // encapsulate the update in the update Class

        UpdateClass updateClass = new CachedWeaponBag(extractWeaponsName(weaponBag), extractLoaded(weaponBag), playerObserver.getPlayerId());

        // send the update to the Virtual View

        playerObserver
                .getTopClass()
                .getController()
                .getVirtualView(playerId)
                .sendUpdates(updateClass);
    }

    private List<String> extractWeaponsName(WeaponBag weaponBag){

        return weaponBag
                .getList()
                .stream()
                .map(Weapon::getName)
                .collect(Collectors.toList());
    }

    private List<Boolean> extractLoaded(WeaponBag weaponBag){

        return weaponBag
                .getList()
                .stream()
                .map(Weapon::isLoaded)
                .collect(Collectors.toList());
    }
}
