package it.polimi.ingsw.view;


import it.polimi.ingsw.utils.Directions;
import it.polimi.ingsw.view.actions.JsonAction;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;
import it.polimi.ingsw.view.updates.UpdateClass;

public interface ViewInterface {

    /**
     * When a user calls askMoveValid with a single direction, the server reply back throught this method if
     * the direction asked is valid (no walls nor out of map)
     * @param b true if direction is valid, false otherwise
     */
    void setValidMove(boolean b);

    /**
     * If the user needs to update the cacheModel, it passes throught this method
     * @param update Update to send to the cacheModel
     */
    void sendUpdates(UpdateClass update);


    /**
     * Start the game in the selected UserInterface
     */
    //STARTGAME to switch gui to the main game window from lobby
    void startGame();

    //SPAWN

    /**
     * Forward the startSpawn method invocation to the client userInterface
     */
    void startSpawn();

    /**
     * Forward the PowerUp chosen by the player to discard to spawn
     * @param powerUp powerUp chosen by the user to spawn in the same color spawn cell
     */
    void spawn(CachedPowerUp powerUp);

    //POWERUP

    /**
     * Forward the startPowerUp method invocation to the client
     */
    void startPowerUp();

    /**
     * Forward the askGrenade method invocation to the client
     */
    void askGrenade();

    //ACTION

    /**
     * Forward the startAction method invocation to the client
     */
    void startAction(boolean isFrenzy, boolean isBeforeFrenzyStarter);


    /**
     * This helper method is used to let the client ask if a single direction is valid, so it doesn't have to
     * know all map adjacences in local, but it just asks the server every time he needs
     * @param row index of the matrix inside the Map
     * @param column index of the column inside the Map
     * @param direction Direction (N,S,E,O) where the player wants to move from his current position
     * @return true if the direction is valid, false otherwise
     */
    boolean askMoveValid(int row, int column, Directions direction);

    /**
     * This method will ask the player to redo just the shoot part of the FrenzyShoot
     */
    void reDoFrenzyAtomicShoot();

    //RELOAD

    /**
     * Forward the startReload method invocation to the client
     */
    void startReload();

    //SHOW -> when controller needs to show msg on the client UI

    /**
     * Helper method to show messages to the user, if needed, for example if client tries to do an invalid action
     * (even thought some of the checks will be done locally) the controller will let the user redo the whole action
     * and send him a message that contains the reason why he can't
     * @param s string to show to the user
     */
    void show(String s);

    /**
     *  used for actions submitted by the client
     * @param jsonAction is the action that the clients want to do
     */
    void doAction(JsonAction jsonAction);

    /**
     * close the view, displaying a default message, used after the action timer has expired to close the client connection
     */
    void close();

    /**
     * This method will show the end game screen
     */
    void endGame();

}
