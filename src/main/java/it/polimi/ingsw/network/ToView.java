package it.polimi.ingsw.network;

import it.polimi.ingsw.view.updates.UpdateClass;

public interface ToView {

    // calls the same method on view Class
    void startSpawn();

    void startPowerUp();

    void startAction();

    void startReload();

    void sendUpdates(UpdateClass update);
}
