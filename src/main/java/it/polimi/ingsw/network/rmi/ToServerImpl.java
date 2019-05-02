package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ToServerImpl implements ToServer{

    private final RMIServer server;

    public ToServerImpl(RMIServer server) throws RemoteException{

        this.server = server;

        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public int joinGame(String name, PlayerColor color)  {

        return server.joinGame(name,color);
    }

    @Override
    public void voteMapType(int mapType)  {

        server.voteMap(mapType);

    }

    @Override
    public Boolean ping() {
        return true;
    }


}
