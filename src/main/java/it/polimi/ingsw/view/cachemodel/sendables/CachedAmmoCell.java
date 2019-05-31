package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.ammo.AmmoCube;
import it.polimi.ingsw.model.map.AmmoCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CachedCell;
import it.polimi.ingsw.view.cachemodel.cachedmap.CellType;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class CachedAmmoCell extends CachedCell {

    private final List<Color> ammoList;
    private final Boolean powerUp;
    private final Point position;

    public CachedAmmoCell(AmmoCell ammoCell) {

        super(CellType.AMMO);

        this.ammoList = ammoCell
                .getAmmoPlaced()
                .getAmmoList()
                .stream()
                .map(AmmoCube::getColor)
                .collect(Collectors.toList());

        this.powerUp = ammoCell
                .getAmmoPlaced()
                .getPowerUp();

        this.position=Model.getMap().cellToCoord(ammoCell);


    }

    public List<Color> getAmmoList() {
        return ammoList;
    }

    public Boolean hasPowerUp() {
        return powerUp;
    }

    public Point getPosition() {
        return position;
    }
}
