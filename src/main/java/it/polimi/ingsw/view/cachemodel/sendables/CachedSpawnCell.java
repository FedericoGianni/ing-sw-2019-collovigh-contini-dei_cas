package it.polimi.ingsw.view.cachemodel.sendables;


import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;

/**
 * This class contains a simplified version of the Spawn cell in model, to store the informations about
 * weapons which can be bought
 */
public class CachedSpawnCell extends CachedCell {

    /**
     * List of names of the weapons which can be bought in this spawn cell
     */
    private final List<String> weaponNames;

    public CachedSpawnCell(List<String> weaponNames, Point position) {

        super(CellType.SPAWN, position);

        this.weaponNames = weaponNames;
    }

    public List<String> getWeaponNames() {
        return weaponNames;
    }
}
