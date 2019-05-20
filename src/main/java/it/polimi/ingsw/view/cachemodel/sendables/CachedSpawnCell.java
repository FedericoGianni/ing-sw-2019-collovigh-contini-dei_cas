package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.map.SpawnCell;
import it.polimi.ingsw.model.weapons.Weapon;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CachedSpawnCell extends CachedCell {

    private final List<String> weaponNames;
    private final Point position;

    public CachedSpawnCell(SpawnCell spawnCell) {

        super(CellType.SPAWN);

        this.weaponNames = spawnCell
                .getWeapons()
                .stream()
                .map(Weapon::getName)
                .collect(Collectors.toList());

        this.position = Model.getMap().cellToCoord(spawnCell);
    }

    public List<String> getWeaponNames() {
        return weaponNames;
    }

    public Point getPosition() {
        return position;
    }
}
