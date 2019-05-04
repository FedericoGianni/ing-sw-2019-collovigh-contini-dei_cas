package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToServer extends Remote {

    int joinGame(String name, PlayerColor color) throws RemoteException;

    void voteMapType(int mapType) throws RemoteException;

    /**
     * This method will register the client address to the server making it reachable
     * @param address is the id of the local registry
     * @param playerId is the id of the player
     * @throws RemoteException
     */
    void registerMe(String address, int playerId, String name) throws RemoteException;

    Boolean ping() throws RemoteException;


}
