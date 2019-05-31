package it.polimi.ingsw.view.cachemodel.sendables;


import it.polimi.ingsw.view.updates.UpdateClass;
import it.polimi.ingsw.view.updates.UpdateType;
import java.util.List;

public class CachedLobby extends UpdateClass {

    private final List<String> names;

    public CachedLobby(List<String> names) {

        super(UpdateType.LOBBY);

        this.names = names;

    }

    public List<String> getNames() {
        return names;
    }
}
