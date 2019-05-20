package it.polimi.ingsw.view.updates;

import it.polimi.ingsw.model.player.PlayerColor;

import java.io.Serializable;
import java.util.List;

public class InitialUpdate implements Serializable, Update{

    private final List<String> names;
    private final List<PlayerColor> colors;
    private final int mapType;
    private final int gameId;

    public InitialUpdate(List<String> names, List<PlayerColor> colors,int gameId, int mapType) {
        this.names = names;
        this.colors = colors;
        this.mapType = mapType;
        this.gameId = gameId;
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

    public int getGameId() {
        return gameId;
    }
}
