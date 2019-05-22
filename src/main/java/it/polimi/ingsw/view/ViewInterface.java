package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.map.Directions;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.cachemodel.updates.UpdateClass;

public interface ViewInterface {

    void sendUpdates(UpdateClass update);

    //SPAWN
    void startSpawn();
    void spawn(CachedPowerUp powerUp);

    //POWERUP
    void startPowerUp();
    void useNewton(Color color, int playerId, Directions directions, int amount);
    void useTeleport(Color color, int r, int c);
    void useMarker(Color color, int playerId);

    //ACTION
    void startAction();

    //RELOAD
    void startReload();

}
