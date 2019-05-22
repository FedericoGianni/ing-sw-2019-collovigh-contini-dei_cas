package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.player.WeaponBag;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.cachemodel.updates.Update;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CachedWeaponBag implements Serializable, Update {

    private final List<String> weapons;

    public CachedWeaponBag(WeaponBag bag) {

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
