package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;

public class CachedAmmoCell extends CachedCell {

    private final List<Color> ammoList;
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
