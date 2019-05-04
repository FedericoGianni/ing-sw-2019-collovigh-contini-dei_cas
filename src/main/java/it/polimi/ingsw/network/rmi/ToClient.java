package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToClient extends Remote {

    void NameAlreadyTaken(String name) throws RemoteException;

    void ColorAlreadyTaken(PlayerColor color) throws RemoteException;

    Boolean ping() throws RemoteException;
}
