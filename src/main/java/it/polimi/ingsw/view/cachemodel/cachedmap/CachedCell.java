package it.polimi.ingsw.view.cachemodel.cachedmap;

import it.polimi.ingsw.view.updates.Update;
import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.awt.*;
import java.io.Serializable;

public abstract class CachedCell extends UpdateClass {

    private final CellType type;

    public CachedCell(CellType type) {

        super((type.equals(CellType.SPAWN) ? UpdateType.SPAWN_CELL : UpdateType.AMMO_CELL));

        this.type = type;
    }

    public CellType getCellType() {
        return type;
    }

    public abstract Point getPosition();
}
