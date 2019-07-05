package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;

/**
 * Simplified version of the Ammo Cell inside model to store the ammo card inside the cells
 */
public class CachedAmmoCell extends CachedCell {

    /**
     * List of ammo inside this ammo cell, will be empty if someone grab ammo
     */
    private final List<Color> ammoList;

    /**
     * True if the ammo card placed in this cell contains a powerup, false otherwise
     */
    private final Boolean powerUp;

    public CachedAmmoCell(List<Color> ammoList, Boolean powerUp, Point position ) {

        super(CellType.AMMO, position);

        this.ammoList = ammoList;

        this.powerUp = powerUp;

    }

    public List<Color> getAmmoList() {
        return ammoList;
    }

    public Boolean hasPowerUp() {
        return powerUp;
    }
}
