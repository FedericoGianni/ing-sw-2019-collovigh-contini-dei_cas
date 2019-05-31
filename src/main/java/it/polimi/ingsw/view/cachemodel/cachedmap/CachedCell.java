package it.polimi.ingsw.view.cachemodel.cachedmap;

import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.awt.*;
import java.io.Serializable;

public abstract class CachedCell extends UpdateClass {

    private final CellType cellType;

    public CachedCell(CellType type) {

        super((type.equals(CellType.SPAWN) ? UpdateType.SPAWN_CELL : UpdateType.AMMO_CELL));

        this.cellType = type;
    }

    public CellType getCellType() {
        return cellType;
    }

    public abstract Point getPosition();
}
