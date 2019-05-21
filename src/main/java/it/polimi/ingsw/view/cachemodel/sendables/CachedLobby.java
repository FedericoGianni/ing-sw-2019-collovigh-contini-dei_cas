package it.polimi.ingsw.view.cachemodel.sendables;

import it.polimi.ingsw.view.updates.Update;

import java.io.Serializable;
import java.util.List;

public class CachedLobby implements Update, Serializable {

    private final List<String> names;

    public CachedLobby(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }
}
