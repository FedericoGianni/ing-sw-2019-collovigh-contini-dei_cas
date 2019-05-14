package it.polimi.ingsw.view.cacheModel;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.List;

public class InitialUpdate implements Serializable {

    private final List<String> names;
    private final List<PlayerColor> colors;
    private final int mapType;

    public InitialUpdate(List<String> names, List<PlayerColor> colors, int mapType) {
        this.names = names;
        this.colors = colors;
        this.mapType = mapType;
    }

    public List<String> getNames() {
        return names;
    }

    public List<PlayerColor> getColors() {
        return colors;
    }

    public int getMapType() {
        return mapType;
    }
}
