package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.networkexceptions.ColorAlreadyTakenException;
import it.polimi.ingsw.network.networkexceptions.NameAlreadyTakenException;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToServerImpl implements ToServer{

    private static final Logger LOGGER = Logger.getLogger("infoLogging");
    private static Level level = Level.FINE;

    private final RMIServer server;

    public ToServerImpl(RMIServer server) throws RemoteException{

        this.server = server;

        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public int joinGame(String name, PlayerColor color) throws RemoteException {

        try {

            LOGGER.log(level,"[RMI-Server] adding new player w/ name: {0}", name);

            return Server.getWaitingRoom().addPlayer(name, color);

        }catch (NameAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"[RMI-Server]Attempted login with name: " +e.getName() + "but name was already used", e);
        }catch (ColorAlreadyTakenException e){
            LOGGER.log(Level.WARNING,"[RMI-Server]Attempted login with color: " +e.getColor() + "but color was already used", e);
        }

        return -1;
    }

    @Override
    public void voteMapType(int mapType) throws RemoteException {

        Server.getWaitingRoom().setMapType(mapType);

        LOGGER.log(level, "A player voted for map: {0} ", mapType );

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
