package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToServer extends Remote {

    int joinGame(String name, PlayerColor color);

    void voteMapType(int mapType);

    Boolean ping();


}
