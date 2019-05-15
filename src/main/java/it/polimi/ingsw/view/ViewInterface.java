package it.polimi.ingsw.view;

import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public interface ViewInterface {

    void startPhase0();
    void spawn(CachedPowerUp powerUp);

    void sendUpdates(UpdateClass update);
}
