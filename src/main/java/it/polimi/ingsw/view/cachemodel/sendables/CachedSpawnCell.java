package it.polimi.ingsw.view.cachemodel.sendables;


import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;
import java.awt.*;
import java.util.List;

public class CachedSpawnCell extends CachedCell {

    private final List<String> weaponNames;

    public CachedSpawnCell(List<String> weaponNames, Point position) {

        super(CellType.SPAWN, position);

        this.weaponNames = weaponNames;
    }

    public List<String> getWeaponNames() {
        return weaponNames;
    }
}
