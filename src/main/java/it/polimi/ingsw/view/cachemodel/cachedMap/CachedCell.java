package it.polimi.ingsw.view.cachemodel.cachedmap;

import it.polimi.ingsw.view.updates.Update;

import java.awt.*;
import java.io.Serializable;

public abstract class CachedCell implements Serializable, Update {

    private final CellType type;

    public CachedCell(CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public abstract Point getPosition();
}
