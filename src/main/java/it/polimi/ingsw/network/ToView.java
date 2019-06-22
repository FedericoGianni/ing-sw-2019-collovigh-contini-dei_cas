package it.polimi.ingsw.network;

import it.polimi.ingsw.view.updates.UpdateClass;

public interface ToView  {

    // Update Logic

    void sendUpdate(UpdateClass update);

    void startGame();

    // calls the same method on view Class

    /**
     * This methods starts the spawn Phase on the client ( 0)
     */
    void startSpawn();

    /**
     * This method starts the "use power Up" phase ( 1, 3, 5)
     */
    void startPowerUp();

    /**
     * This method starts the action phase on the client ( 2, 4)
     */
    void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter);

    /**
     * This method will ask the player to redo just the shoot part of the FrenzyShoot
     */
    void reDoFrenzyAtomicShoot();

    /**
     * This method starts the reload phase of the turn ( 6)
     */
    void startReload();

    /**
     * This method will be called on a player if he/she was shot in the previous phase and has grenades
     */
    void askGrenade();

    /**
     * This method is used by the controller to show msg on the ui if needed
     * @param s message to show on the user interface
     */
    void show(String s);

    /**
     * This method will show the end game screen
     */
    void endGame();

}
