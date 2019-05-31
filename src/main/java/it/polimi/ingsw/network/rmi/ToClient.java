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
    void NameAlreadyTaken(String name) throws RemoteException;

    /**
     * This method notify the user that the color chosen is already taken
     * @param color is the color that was chosen by the user
     * @throws RemoteException
     */
    void ColorAlreadyTaken(PlayerColor color) throws RemoteException;

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

    void startGame() throws RemoteException;

    void sendUpdate(UpdateClass update) throws RemoteException;


    // Turn Handling

    void startSpawn() throws RemoteException;


    void startPowerUp() throws RemoteException;


    void startAction() throws RemoteException;


    void startReload()throws RemoteException;


    void askGrenade()throws RemoteException;

}
