package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class RMIServer implements Runnable,RemoteServer {

    public RMIServer() throws RemoteException{



        UnicastRemoteObject.exportObject(this, 0);

    }

    @Override
    public void run() {
        Logger.getLogger("infoLogger").info("Starting RMIServer");
    }

    @Override
    public int login(String name, PlayerColor color) throws RemoteException {

        try {
            return Server.getWaitingRoom().addPlayer(name, color);
        }catch(NameAlreadyTakenException e){

            e.printStackTrace();

        }catch (ColorAlreadyTakenException e){

            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void voteMap(int mapType) throws RemoteException {

        if (Server.getWaitingRoom().isActive()) Server.getWaitingRoom().setMapType(mapType);

    }
}
