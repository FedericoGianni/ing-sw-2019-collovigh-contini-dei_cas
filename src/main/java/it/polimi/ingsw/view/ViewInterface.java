package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public interface ViewInterface {

    void sendUpdates(UpdateClass update);

    //PHASE0
    void startPhase0();
    void spawn(CachedPowerUp powerUp);

    //PHASE1
    void startPhase1();
    void useNewton(Color color, int playerId, Directions directions, int amount);
    void useTeleport(Color color, int r, int c);
    void useMarker(Color color, int playerId);

    //ACTION1
    void startAction1();
}
