package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.updates.UpdateClass;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToClient extends  Remote {

    /**
     * This method notify the user that the name chosen is already taken
     * @param name is the name that was chosen by the user
     * @throws RemoteException
     */
    void nameAlreadyTaken(String name) throws RemoteException;

    /**
     * This method notify the user that the color chosen is already taken
     * @param color is the color that was chosen by the user
     * @throws RemoteException
     */
    void colorAlreadyTaken(PlayerColor color) throws RemoteException;

    /**
     *
     * @return always true
     * @throws RemoteException
     */
    Boolean ping() throws RemoteException;

    /**
     * @return the player Id of the user
     * @throws RemoteException
     */
    int getPid() throws RemoteException;


    /**
     *
     * @param gameId is the id of the game
     * @throws RemoteException
     */
    void initGame(int gameId) throws RemoteException;

    //Updates

    /**
     * This method will notify the inactive players that the game has started
     * @throws RemoteException
     */
    void startGame() throws RemoteException;

    /**
     * this method is used to send updates to client's cacheModel
     * @param update is the update class
     * @throws RemoteException
     */
    void sendUpdate(UpdateClass update) throws RemoteException;


    // Turn Handling

    /**
     * This method will start the spawn Phase on the client
     * @throws RemoteException
     */
    void startSpawn() throws RemoteException;

    /**
     * This method will start the power Up Phase on the client
     * @throws RemoteException
     */
    void startPowerUp() throws RemoteException;

    /**
     * This method will start the action Phase on the client
     * @throws RemoteException
     */
    void startAction() throws RemoteException;

    /**
     * This method will start the reload Phase on the client
     * @throws RemoteException
     */
    void startReload()throws RemoteException;

    /**
     * This method will start the grenade Phase on the client
     * @throws RemoteException
     */
    void askGrenade()throws RemoteException;

    /**
     * This method will call the show method on the view
     * @param s is the message to show
     * @throws RemoteException
     */
    void show(String s) throws RemoteException;

    /**
     * This method will notify the client of the end game
     * @throws RemoteException
     */
    void endGame() throws RemoteException;

}
