package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToClientImpl implements ToClient {

    private final RMIClient client;

    public ToClientImpl(RMIClient client) throws RemoteException {

        this.client = client;

        UnicastRemoteObject.exportObject(this, 0);

    }

    @Override
    public void NameAlreadyTaken(String name) throws RemoteException{


    }

    @Override
    public void ColorAlreadyTaken(PlayerColor color) throws RemoteException{

    }

    @Override
    public Boolean ping() throws RemoteException {
        return true;
    }


}
