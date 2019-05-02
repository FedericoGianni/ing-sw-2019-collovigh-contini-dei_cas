package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {

    int login(String name, PlayerColor color) throws RemoteException;

    void voteMap(int mapType) throws RemoteException;
}
