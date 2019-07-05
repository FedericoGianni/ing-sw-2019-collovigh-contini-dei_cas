package it.polimi.ingsw.view.cachemodel.sendables;


import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;

import java.util.List;

/**
 * Class to store the updates sent from server which stores the names of the currently connected players waiting to join the game
 */
public class CachedLobby extends UpdateClass {

    /**
     * Name of the players waiting to join the game
     */
    private final List<String> names;

    public CachedLobby(List<String> names) {

        super(UpdateType.LOBBY);

        this.names = names;

    }

    public List<String> getNames() {
        return names;
    }
}
