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

    /**
     * {@inheritDoc}
     */
    @Override
    public void NameAlreadyTaken(String name) throws RemoteException{


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ColorAlreadyTaken(PlayerColor color) throws RemoteException{

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean ping() throws RemoteException {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPid() throws RemoteException {

        return client.getPlayerId();
    }


}
