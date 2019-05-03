package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToServerImpl implements ToServer{

    private final RMIServer server;

    public ToServerImpl(RMIServer server) throws RemoteException{

        this.server = server;

        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public int joinGame(String name, PlayerColor color) throws RemoteException {

        return server.joinGame(name,color);
    }

    @Override
    public void voteMapType(int mapType) throws RemoteException {

        server.voteMap(mapType);

    }

    @Override
    public Boolean ping() throws RemoteException {
        return true;
    }


    public void unExport(){

        try {

            UnicastRemoteObject.unexportObject(this, true);

        }catch (NoSuchObjectException e){

            e.printStackTrace();
        }
    }
}
