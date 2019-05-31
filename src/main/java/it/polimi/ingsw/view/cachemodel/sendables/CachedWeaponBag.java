package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.player.WeaponBag;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CachedWeaponBag extends UpdateClass {

    private final List<String> weapons;

    public CachedWeaponBag(WeaponBag bag, int playerId) {

        super(UpdateType.POWERUP_BAG,playerId);

        bag.getList().stream().map(Weapon::getName).collect(Collectors.toList());

        this.weapons = bag
                .getList()
                .stream()
                .map(Weapon::getName)
                .collect(Collectors.toList());
    }

    public List<String> getWeapons() {
        return weapons;
    }
}
