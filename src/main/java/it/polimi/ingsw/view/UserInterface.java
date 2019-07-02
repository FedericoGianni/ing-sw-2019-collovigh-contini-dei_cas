package it.polimi.ingsw.view;

import it.polimi.ingsw.view.updates.UpdateType;
import it.polimi.ingsw.view.updates.otherplayerturn.TurnUpdate;

import java.util.List;

public interface UserInterface {

    /**
     *  This function starts the ui and ask the user which protocol wants to use
     */
    void startUI();

    /**
     * Ask mapType and number of skulls to place in the killshotTrack (only to the first player connected)
     * @return a list of 2 integers, the first one is the chosen mapType, the second one the chosen number of skulls
     */
    List<Integer> askMapAndSkulls();

    // Utils

    /**
     * Show a message on the user interface
     * @param s string to show
     */
    void show(String s);

    /**
     * Close the UserInterface, after a timer of the current action phase expires
     */
    void close();

    /**
     * Notify that a local client class has been updated inside the cachemodel
     * @param updateType the class which has been updated
     * @param playerId useful in some case to determine which player has been updated (i.e. new position)
     * @param turnUpdate is the update class if the update is of type Turn
     */
    void notifyUpdate(UpdateType updateType, int playerId, TurnUpdate turnUpdate);

    /**
     * Start the game in the selected user interface
     */
    void startGame();

    /**
     * Method useful for movements. First the client calls askMoveValid, with a single direction, the the
     * server reply back to the client throught this setter, and it sets validMove to true if the direction is valid
     * (no walls, nor out of map), otherwise false
     * @param b boolean, true if direction is valid, false otherwise
     */
    void setValidMove(boolean b);


    // Login

    void gameSelection();

    /**
     * Method that handles the player login
     */
    void login();

    /**
     * If the above method throws an error, it shows the error and let the user retry to login
     * @param error
     */
    void retryLogin(String error);

    /**
     * Same as above, but with custom exceptions instead of String as error
     * @param e custom exception to represent why login wasn't successful
     */
    void retryLogin(Exception e);


    // Game Management

    /**
     * Handles the user spawn, asking for a powerUp to discard and forwarding it to the view linked to this UI
     */
    void startSpawn();

    /**
     * Handles the user powerUp phase, in which it let the user choose if he wants to use a PowerUp or skip this phase
     */
    void startPowerUp();

    /**
     * When a user takes damage from another player, it will be asked if he wants to use this powerUp
     */
    void askGrenade();

    /**
     * StartAction method, in which the user can choose between the game actions: move, move&grab, shoot
     * @param isFrenzy if is set to true start Frenzy methods, otherwise normal actions
     * @param isBeforeFrenzyStarter if true -> current player is before frenzy starter, otherwise false
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
     * Last turn phase, in which the user is asked if he wants to reload one or more of his weapons
     */
    void startReload();

    /**
     * This method will show the end game screen
     */
    void endGame();

}
