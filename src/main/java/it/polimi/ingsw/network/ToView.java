package it.polimi.ingsw.network;

import it.polimi.ingsw.view.updates.UpdateClass;

import java.util.List;

public interface ToView  {

    // Update Logic

    /**
     * send an update to inform the UserInterface of a change inside cacheModel
     * @param update update to be sent to the view
     */
    void sendUpdate(UpdateClass update);

    /**
     * Forward startGame call to the UserInterface to inform the players that the game has started
     */
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
    void doFrenzyAtomicShoot();

    /**
     * This method will ask the player to do the reload atomic action for the frenzy shoot
     */
    void doFrenzyReload();

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

    /**
     * Close the UserInterface, after a timer of the current action phase expires
     */
    void close();

    /**
     * Forward the askMapAndSkulls metod invocation to the UserInterface
     * @return a list of Integers containing: [0] mapType chosen, [1] number of skulls to place in the KillShotTrack
     */
    List<Integer> askMapAndSkulls();

}
