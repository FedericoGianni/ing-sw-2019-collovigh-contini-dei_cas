package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToServer extends Remote {

    int joinGame(String name, PlayerColor color) throws RemoteException;

    void voteMapType(int mapType) throws RemoteException;

    Boolean ping() throws RemoteException;


}
