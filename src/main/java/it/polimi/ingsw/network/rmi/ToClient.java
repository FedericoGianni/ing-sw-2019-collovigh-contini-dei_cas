package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.cachemodel.CachedPowerUp;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToClient extends Remote {

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

    /**
     *
     * @param phaseNum is the phase number
     *                 0 -> Spawn
     *                 1 -> powerUp 1
     *                 2 -> action 1
     *                 3 -> powerUp 2
     *                 4 -> action 2
     *                 5 -> powerUp 3
     *                 6 -> reload
     * @throws RemoteException
     */
    void startPhase(int phaseNum) throws RemoteException;


    /**
     *
     * @return the grenade that will be used or null if not
     * @throws RemoteException
     */
    CachedPowerUp useGranade() throws RemoteException;



}
