package it.polimi.ingsw.view.cachemodel.cachedmap;


import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import java.awt.*;

public abstract class CachedCell extends UpdateClass {

    private final CellType cellType;
    private final Point position;

    public CachedCell(CellType type, Point position) {

        super((type.equals(CellType.SPAWN) ? UpdateType.SPAWN_CELL : UpdateType.AMMO_CELL));

        this.cellType = type;

        this.position = position;
    }

    public CellType getCellType() {
        return cellType;
    }

    public Point getPosition() {
        return position;
    }
}
